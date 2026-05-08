package dev.pedrohfreitas.javalib.infrastructure.telegram;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriedades de configuracao do Telegram Bot.
 */
@ConfigurationProperties(prefix = "java-lib.telegram")
public record TelegramBotProperties(
        String botToken,
        String apiUrl,
        Retry retry,
        java.time.Duration connectTimeout,
        java.time.Duration readTimeout
) {
    public record Retry(
            int maxAttempts,
            java.time.Duration initialBackoff,
            java.time.Duration maxBackoff
    ) {}
}
