package ru.gazprombank.ssdailybot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import ru.gazprombank.ssdailybot.dto.NotificationInfo;
import ru.gazprombank.ssdailybot.service.ReminderManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Configuration
public class NotificationInfoConfig {

    @Autowired
    private TaskScheduler executor;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ApplicationContext appContext;
    @Autowired
    private ReminderManager reminderManager;

    @Value("${dailybot.notification-config-path}")
    private String notificationConfigPath;

    @EventListener(ApplicationStartedEvent.class)
    public void scheduleNotificationInfos() {
        File notificationConfigsFolder = new File(notificationConfigPath);

        File[] notificationInfoFiles = Objects.requireNonNull(
                notificationConfigsFolder.listFiles(),
                "Не удалось прочитать путь к файлам с напоминаниями: " + notificationConfigsFolder
        );

        List<NotificationInfo> notificationInfos = Arrays.stream(notificationInfoFiles)
                .flatMap(file -> readNotificationInfoFromFile(file).stream())
                .filter(NotificationInfo::isActive)
                .peek(ni -> log.info("Успешно добавлено напоминание: {}", ni.getName()))
                .toList();

        if (notificationInfos.isEmpty()) {
            log.error("Не удалось прочитать ни один файл с напоминаниями, работа приложения будет прекращена");

            int exitCode = SpringApplication.exit(appContext, () -> 0);
            System.exit(exitCode);
        }

        // TODO validate notificationInfos

        notificationInfos.forEach(notificationInfo -> executor.schedule(
                () -> reminderManager.processNotificationInfo(false, false, notificationInfo),
                new CronTrigger(notificationInfo.getCron())));
    }

    private Optional<NotificationInfo> readNotificationInfoFromFile(File file) {
        Path path = file.toPath();

        try {
            String rawNotificationInfo = Files.readString(path);

            return Optional.of(objectMapper.readValue(rawNotificationInfo, NotificationInfo.class));
        } catch (IOException e) {
            log.error("Не удалось прочитать файл с информацией об уведомлении: {}", e.getMessage(), e);

            return Optional.empty();
        }
    }
}
