package com.github.javalib.infrastructure.telegram.config;

import com.github.javalib.infrastructure.telegram.TelegramBotClient;
import com.github.javalib.infrastructure.telegram.TelegramBotProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Auto-configuracao do cliente Telegram Bot.
 * Ativada apenas quando java-lib.telegram.bot-token esta configurado.
 */
@AutoConfiguration
@ConditionalOnProperty("java-lib.telegram.bot-token")
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramConfig {

    @Bean
    public TelegramBotClient telegramBotClient(TelegramBotProperties props, WebClient.Builder builder) {
        WebClient webClient = builder
                .baseUrl(props.apiUrl() + props.botToken() + "/")
                .build();

        return new TelegramBotClient(webClient, props);
    }
}
