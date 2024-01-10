package ru.gazprombank.ssdailybot.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gazprombank.ssdailybot.service.FortuneWheelService;

@Slf4j
@Service
@Profile("disabled") // Бин отключен
public class WheelCommand extends BotCommand {

    private final FortuneWheelService fortuneWheelService;

    public WheelCommand(FortuneWheelService fortuneWheelService) {
        super("wheel", "Choose a speaker");
        this.fortuneWheelService = fortuneWheelService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chat.getId());

        String congratulationMsg = fortuneWheelService.createReadyMsg();

        sendMessage.setText(congratulationMsg);

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке wheel сообщения {}: {}", chat.getId(), e.getMessage(), e);
        }
    }
}