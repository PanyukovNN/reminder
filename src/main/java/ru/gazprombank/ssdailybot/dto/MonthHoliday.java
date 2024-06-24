package ru.gazprombank.ssdailybot.dto;

import lombok.Data;

@Data
public class MonthHoliday {

    /**
     * Номер месяца, начиная с 1
     */
    private int month;
    /**
     * Выходные через запятую
     */
    private String days;
}
