package ru.gazprombank.ssdailybot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gazprombank.ssdailybot.service.DayBotManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DayBotController {

    private final DayBotManager dayBotManager;

    @GetMapping("/send-message")
    public void sendMessage(@RequestParam(value = "force", required = false) boolean isForce) {
        dayBotManager.processSending(isForce, false);
    }

    @GetMapping("/send-message-debug")
    public void sendMessageDebug() {
        dayBotManager.processSending(true, true);
    }
}
