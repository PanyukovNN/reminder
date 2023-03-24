package ru.gazprombank.ssdailybot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.gazprombank.ssdailybot.property.DayBotProperty;
import ru.gazprombank.ssdailybot.service.messagesender.MessageSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DayBotManager {

    private static final String DAILY_LINK_LINE = "Подключаемся по <a href='%s'>ссылке</a>";

    private final MessageSender messageSender;
    private final MessagePicker messagePicker;
    private final ExecutionTimeChecker timeChecker;
    private final DayBotProperty dayBotProperty;

    public void processSending(boolean isForce, boolean isDebug) {
        if (!isForce) {
            timeChecker.check();
        }

        String chatId = isDebug
                ? dayBotProperty.getDebugChatId()
                : dayBotProperty.getChatId();

        messageSender.sendSticker(chatId, messagePicker.pickDailyStickerFileId())
                .onErrorResume(e -> {
                    log.error("Непредвиденная ошибка при отправке стикера: " + e.getMessage(), e);

                    return Mono.empty();
                })
                .then(messageSender.sendMessage(chatId, formatMessage())
                        .doOnSuccess(item -> log.info("Отправка напоминания выполнена успешно.")))
                .onErrorResume(e -> {
                    String errMsg = "Возникла ошибка при отправке сообщения: " + e.getMessage();

                    log.error(errMsg, e);

                    return messageSender.sendMessage(dayBotProperty.getDebugChatId(), errMsg)
                            .doOnError(ex -> log.info("Произошла ошибка при отрпавке debug сообщения: {}", ex.getMessage(), ex))
                            .doOnSuccess(item -> log.info("Отправка информации об исключительной ситуации выполнена успешно."));
                })
                .subscribe();
    }

    @Scheduled(cron = "0 */10 8-10 * * MON-FRI")
    public void scheduled() {
        log.info("Scheduler iteration");
    }

    private String formatMessage() {
        List<String> messageLines = new ArrayList<>();

        messageLines.add(messagePicker.pickDailyMessage());
        messageLines.add(Strings.EMPTY);
        messageLines.add(String.format(DAILY_LINK_LINE, dayBotProperty.getZoomLink()));

        return String.join("\n", messageLines);
    }
}
