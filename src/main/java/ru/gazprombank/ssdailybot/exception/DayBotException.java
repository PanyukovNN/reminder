package ru.gazprombank.ssdailybot.exception;

/**
 * Базовое исключение приложения.
 */
public class DayBotException extends RuntimeException {

    public DayBotException(String message) {
        super(message);
    }

    public DayBotException(String message, Exception e) {
        super(message, e);
    }
}
