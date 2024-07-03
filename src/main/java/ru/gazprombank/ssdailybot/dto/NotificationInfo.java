package ru.gazprombank.ssdailybot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NotificationInfo {

    private String name;
    private boolean active;
    private boolean checkHolidays;
    private String chatId;
    private String cron;
    private List<String> stickers;
    private TextMessageConfig textMessage;

}
