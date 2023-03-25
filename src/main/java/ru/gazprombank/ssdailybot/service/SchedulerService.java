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

    @Scheduled(cron = "${dailybot.cron}")
    public void schedule() {
        log.info("Выполняется запланированный запуск");

        dayBotManager.processSending(true, false);
    }
}
