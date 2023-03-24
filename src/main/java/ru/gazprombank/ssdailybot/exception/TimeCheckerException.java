package ru.gazprombank.ssdailybot.exception;

/**
 * Исключение при возникновении ошибок запуска приложения.
 */
public class TimeCheckerException extends RuntimeException {

    public TimeCheckerException(String message) {
        super(message);
    }
}
