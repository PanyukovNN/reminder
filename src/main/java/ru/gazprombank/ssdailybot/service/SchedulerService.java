package ru.gazprombank.ssdailybot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("scheduler")
@RequiredArgsConstructor
public class SchedulerService {

    private final DayBotManager dayBotManager;

    @Scheduled(cron = "${dailybot.daily-cron}")
    public void daily() {
        log.info("Выполняется запланированный запуск daily сообщения");

        dayBotManager.processSendingDailyMessage(true, false);
    }

    @Scheduled(cron = "${dailybot.fw-cron}")
    public void fortuneWheel() {
        log.info("Выполняется запланированный запуск fortune wheel сообщения");

        dayBotManager.processSendingFortuneWheel();
    }
}
