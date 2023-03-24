package ru.gazprombank.ssdailybot.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MessagePicker {

    private static final List<String> DAILY_MESSAGES = List.of(
            "Друзья, напоминаю, что в 10:00 начинается ежедневная конференция. Это важный момент для нашей команды, так что пожалуйста не опаздывайте.",
            "Приветствую всех! Не забывайте, что сегодня в 10:00 мы начинаем нашу ежедневную конференцию. Пожалуйста, подключайтесь вовремя и готовьтесь к продуктивной работе.",
            "Напоминаю, что сегодня в 10:00 начинается ежедневная конференция. Пожалуйста, подключитесь к ней вовремя.",
            "Доброе утро! Не забывайте, что сегодня в 10:00 мы начинаем ежедневную конференцию. Она очень важна для нашей команды, так что пожалуйста, подключитесь вовремя.",
            "Сегодня в 10:00 начинается наша ежедневная конференция. Она поможет нам оставаться в курсе проекта и координировать нашу работу. Пожалуйста, не пропустите ее."
    );

    private static final List<String> STICKER_FILE_IDS = List.of(
            "CAACAgIAAxUAAWQHUpOe8gjS9xF5ELdbRKIqBT0QAAIgAgACgNjXAlRRuGXUv08_LgQ", // корги и дедлайн
            "CAACAgIAAxUAAWP-UjfJYXzFU-60dN87OseXEQlwAAIhDAACz9ugSkTkoxvMweuALgQ",
            "CAACAgIAAxUAAWP-UjfXBbXnTZOty42w8brkMlQCAAKyDwACpQagSjoYQnDKnHVVLgQ",
            "CAACAgIAAxUAAWP-UjdvbpR8U8lklA1TQQeXleE9AAJEEAAC5jGpSujtaWqSWmc0LgQ",
            "CAACAgIAAxUAAWP-UjckbFpphVbzxykFd1_XQK1-AAIoDQACBj2hSt7e6pf5SgAB2C4E",
            "CAACAgIAAxUAAWP-UjdYqvPTwQ4OCkep_VK55DU4AAJGEAAC0wmoSie8-K9ypkq7LgQ",
            "CAACAgIAAxUAAWP-UjdjV2cP4fj6qp8xoCZIyVoUAAJnDwACzMqgSgEeQCOG3_Q9LgQ",
            "CAACAgIAAxUAAWP-Ujf3aHvRkDYoNXKPwSd_dorAAAKXDAACF3upSt75n2falAeDLgQ",
            "CAACAgIAAxUAAWP-UjdQZ-mCUs3AfndZ1p-Mat2PAAJHDgACL16hSmdHODeliSRGLgQ",
            "CAACAgIAAxUAAWP-UjeO3D4r88qATNhCtv-jgA24AAK0EAACU7ugSscyodvrj0AZLgQ",
            "CAACAgIAAxUAAWP-UjfnkkZF18ZB3vGxetfj7FBBAAJKDgAC9gmoShlHXpK4z_GuLgQ",
            "CAACAgIAAxUAAWP-UjcdW-x5_9dQQ3kQbSxrJgz0AAIBDQACizuhShFstrp3jSX6LgQ",
            "CAACAgIAAxUAAWP-Ujc9WeKdDxoqI9_-dgnN0UtOAAIHDgACn-GoSqW-AYorXdE3LgQ",
            "CAACAgIAAxUAAWP-UjenC0jcIMnXAjb3Oo6E6dJrAAJyDgACUgqpSrdwVhWAoaI3LgQ",
            "CAACAgIAAxUAAWP-UjcZjIFtiV0cO0isdis9X3SmAALkDwACxAOgSpBXjpX2IgriLgQ",
            "CAACAgIAAxUAAWP-UjcydUm33eWGYIkdhFzs90ttAAIIEgACeTqhSpatCFxnhDtoLgQ",
            "CAACAgIAAxUAAWP-Uje007T9_W5jfogIIqO8Fv4lAALcDQACaqyoSjsId9zPH68ZLgQ",
            "CAACAgIAAxUAAWP-UjdOPpcO7UBn8flWTCs6NujNAALTDQAC45-gSqv6pb0da5pWLgQ",
            "CAACAgIAAxUAAWP-UjfP5SaBxzo6hDsaEhBGueSvAAK1DQACbYKoSi668hwTeOdELgQ",
            "CAACAgIAAxUAAWP-Ujcav-ky9i0dKTqp6jHR1hMcAAIDDQACK5CgSqm-ukcfqTCgLgQ"
    );

    public String pickDailyMessage() {
        return chooseRandomDailyMessage();
    }

    public String pickDailyStickerFileId() {
        return chooseRandomStickerFileId();
    }

    private String chooseRandomDailyMessage() {
        int todayMessageIndex = randomIndexByDayOfMonth(DAILY_MESSAGES.size());

        return DAILY_MESSAGES.get(todayMessageIndex);
    }

    private String chooseRandomStickerFileId() {
        int todayMessageIndex = randomIndexByDayOfMonth(STICKER_FILE_IDS.size());

        return STICKER_FILE_IDS.get(todayMessageIndex);
    }

    private int randomIndexByDayOfMonth(int listSize) {
        return LocalDate.now().getDayOfMonth() % listSize;
    }
}
