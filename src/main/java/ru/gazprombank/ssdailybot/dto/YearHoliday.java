package ru.gazprombank.ssdailybot.dto;

import lombok.Data;

import java.util.List;

@Data
public class YearHoliday {

    private int year;
    private List<MonthHoliday> months;
}
