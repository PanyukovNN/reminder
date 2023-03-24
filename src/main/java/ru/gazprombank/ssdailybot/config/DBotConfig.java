package ru.gazprombank.ssdailybot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Общая конфигурация приложения.
 */
@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class DBotConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
