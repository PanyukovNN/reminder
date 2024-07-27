package ru.gazprombank.ssdailybot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.gazprombank.ssdailybot.service.NotificationInfoLoader;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DayBotController {

    private final NotificationInfoLoader notificationInfoLoader;

    @PostMapping("/reload-notifications")
    public Mono<String> reloadNotifications() {
        return notificationInfoLoader.loadNotifications()
                .map(it -> {
                    log.info("Уведомления успешно обновлены");

                    return "Уведомления успешно обновлены";
                })
                .onErrorResume(ex -> {
                    log.error("Ошибка при обновлении уведомлений из репозитория: {}", ex.getMessage(), ex);

                    return Mono.just("Ошибка при обновлении уведомлений из репозитория");
                });
    }
}
