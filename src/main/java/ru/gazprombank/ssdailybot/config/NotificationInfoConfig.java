package ru.gazprombank.ssdailybot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import ru.gazprombank.ssdailybot.dto.GithubFileInfo;
import ru.gazprombank.ssdailybot.dto.NotificationInfo;
import ru.gazprombank.ssdailybot.property.DayBotProperty;
import ru.gazprombank.ssdailybot.service.ReminderManager;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class NotificationInfoConfig {

    @Autowired
    private WebClient webClient;
    @Autowired
    private TaskScheduler executor;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DayBotProperty dayBotProperty;
    @Autowired
    private ApplicationContext appContext;
    @Autowired
    private ReminderManager reminderManager;

    @EventListener(ApplicationStartedEvent.class)
    public void scheduleNotificationInfos() {
        downloadNotificationInfosFromGithub()
                .mapNotNull(rawNotificationInfo -> readNotificationInfo(rawNotificationInfo).orElse(null))
                .filter(NotificationInfo::isActive)
                .doOnNext(notificationInfo -> {
                    log.info("Успешно добавлено напоминание: {}", notificationInfo.getName());

                    // TODO validate notificationInfos

                    executor.schedule(
                            () -> reminderManager.processNotificationInfo(false, false, notificationInfo),
                            new CronTrigger(notificationInfo.getCron()));
                })
                .subscribe();
    }

    private Optional<NotificationInfo> readNotificationInfo(String rawNotificationInfo) {
        try {
            return Optional.of(objectMapper.readValue(rawNotificationInfo, NotificationInfo.class));
        } catch (IOException e) {
            log.error("Не удалось прочитать файл с информацией об уведомлении: {}", e.getMessage(), e);

            return Optional.empty();
        }
    }

    // TODO добавить логи на загрузку конфигов
    private Flux<String> downloadNotificationInfosFromGithub() {
        URI uri = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/PanyukovNN/project-configs/contents/" + dayBotProperty.getNotificationConfigPath())
                .build()
                .toUri();

        Flux<GithubFileInfo> githubFileInfosFlux = webClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + dayBotProperty.getGithubProjectConfigsToken())
                .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GithubFileInfo>>() {
                })
                .flatMapMany(Flux::fromIterable);

        return githubFileInfosFlux
                .flatMap(githubFileInfo -> webClient.get()
                        .uri(githubFileInfo.getDownloadUrl())
                        .retrieve()
                        .bodyToMono(String.class));
    }
}
