package ru.gazprombank.ssdailybot.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.gazprombank.ssdailybot.service.ReminderManager;

@Slf4j
@Service
public class CheckCommand extends BotCommand {

    private final ReminderManager reminderManager;

    public CheckCommand(ReminderManager reminderManager) {
        super("check", "Start command");
        this.reminderManager = reminderManager;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
//        reminderManager.processNotificationInfo(true, false); ЕЩВЩ
    }
}