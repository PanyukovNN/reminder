package ru.gazprombank.ssdailybot.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import static ru.gazprombank.ssdailybot.config.DBotConfig.TG_UPDATE_SINK;

@Slf4j
@Service
@RequiredArgsConstructor
public class DBotListener {

    @EventListener(ApplicationStartedEvent.class)
    public void onStartup() {
        TG_UPDATE_SINK.asFlux()
                .doOnNext(update -> {
                    User user = update.getMessage().getFrom();

                    String username = Optional.ofNullable(user).map(User::getUserName).orElse("undefined");
                    String firstname = Optional.ofNullable(user).map(User::getFirstName).orElse("undefined");
                    String lastname = Optional.ofNullable(user).map(User::getLastName).orElse("undefined");

                    String messageText = Optional.ofNullable(update)
                            .map(Update::getMessage)
                            .map(Message::getText)
                            .orElse("undefined");

                    Long chatId = Optional.ofNullable(update)
                            .map(Update::getMessage)
                            .map(Message::getChatId)
                            .orElse(0L);

                    log.info("Входящее update сообщение в чате: {}. Текст: {}. От: {}", chatId, messageText, username + " " + firstname + " " + lastname + " ");
                })
                .subscribe();
    }
}
