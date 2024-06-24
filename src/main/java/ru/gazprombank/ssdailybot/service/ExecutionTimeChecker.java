package ru.gazprombank.ssdailybot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.gazprombank.ssdailybot.exception.TimeCheckerException;
import ru.gazprombank.ssdailybot.property.DayBotProperty;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

/**
 * Сервис проверки времени запуска.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutionTimeChecker {

    private static final LocalTime START_TIME = LocalTime.of(9, 58);
    private static final LocalTime END_TIME = LocalTime.of(10, 2);
    private static final List<DayOfWeek> WEEK_DAY_BLACK_LIST = List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    private static final DateTimeFormatter FRONT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Set<LocalDate> holidays;
    private final DayBotProperty dayBotProperty;

    /**
     * Проверяет, что текущее время подпадает под допустимый период.
     */
    public void dailyMessageCheck() {
        if (!dayBotProperty.isCheckExecutionTime()) {
            log.info("Проверка времени запуска бота отключена");

            return;
        }

        checkHoliday();

        checkTime();

        log.info("Проверка времени запуска выполнена успешно");
    }

    private void checkHoliday() {
        // Если не удалось распознать производственный календарь из ресурсов, то проверяем будний ли сегодня день
        if (CollectionUtils.isEmpty(holidays) && WEEK_DAY_BLACK_LIST.contains(LocalDate.now().getDayOfWeek())) {
            String errMsg = String.format("Нерабочий день, не допускается запуск по следующим дням: %s", WEEK_DAY_BLACK_LIST);

            throw new TimeCheckerException(errMsg);
        }

        if (holidays.contains(LocalDate.now())) {
            throw new TimeCheckerException("Выходной день по производственному календарю, запуск отменен");
        }
    }

    private void checkTime() {
        LocalTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                .toLocalTime();

        if (currentTime.isBefore(START_TIME)) {
            String errMsg = String.format("Ранний запуск. Текущее время %s, время начало периода %s",
                    FRONT_TIME_FORMATTER.format(currentTime), START_TIME);

            throw new TimeCheckerException(errMsg);
        }

        if (currentTime.isAfter(END_TIME)) {
            String errMsg = String.format("Поздний запуск. Текущее время %s, время окончания периода %s",
                    FRONT_TIME_FORMATTER.format(currentTime), END_TIME);

            throw new TimeCheckerException(errMsg);
        }
    }
}
