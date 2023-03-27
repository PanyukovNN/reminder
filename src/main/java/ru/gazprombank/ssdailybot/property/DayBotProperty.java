package ru.gazprombank.ssdailybot.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;

@Slf4j
@Getter
@Setter
@ToString
@Component
@Validated
@ConfigurationProperties("dailybot")
public class DayBotProperty {

    /**
     * Токен телеграм бота.
     */
    @NotEmpty(message = "Не задан токен телеграм бота")
    private String botToken;
    /**
     * Идентификатор чата рассылки.
     */
    @NotEmpty(message = "Не задан идентификатор чата рассылки")
    private String chatId;
    /**
     * Идентификатор чата для информирования об исключительных ситуациях.
     */
    @NotEmpty(message = "Не задан идентификатор чата для информирования об исключительных ситуациях")
    private String debugChatId;
    /**
     * Признак отправки сообщения.
     */
    private boolean sendMessages;
    /**
     * Признак проверки точного времени отправки сообщения.
     */
    private boolean checkExecutionTime;
    /**
     * Ссылка на zoom.
     */
    private String zoomLink;

    private String cron;

    @PostConstruct
    public void pc() {
        log.info(this.toString());
    }
}
