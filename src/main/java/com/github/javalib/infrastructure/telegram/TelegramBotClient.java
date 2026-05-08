package com.github.javalib.infrastructure.telegram;

import com.github.javalib.domain.exception.ExternalServiceException;
import com.github.javalib.domain.model.TelegramBotInfo;
import com.github.javalib.domain.model.TelegramMessage;
import com.github.javalib.domain.port.TelegramBotPort;
import com.github.javalib.infrastructure.telegram.dto.GetMeResponse;
import com.github.javalib.infrastructure.telegram.dto.SendMessageRequest;
import com.github.javalib.infrastructure.telegram.dto.SendMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * Cliente HTTP para o Telegram Bot API usando WebClient.
 */
@Slf4j
public class TelegramBotClient implements TelegramBotPort {

    private static final String SERVICE_NAME = "Telegram";

    private final WebClient webClient;
    private final TelegramBotProperties properties;

    public TelegramBotClient(WebClient webClient, TelegramBotProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    /**
     * Cria um cliente Telegram programaticamente, sem necessidade de Spring.
     * O WebClient eh construido internamente com a URL base correta.
     */
    public static TelegramBotClient create(TelegramBotProperties properties) {
        WebClient webClient = WebClient.builder()
                .baseUrl(properties.apiUrl() + properties.botToken() + "/")
                .build();
        return new TelegramBotClient(webClient, properties);
    }

    @Override
    public boolean sendMessage(TelegramMessage message) {
        var request = new SendMessageRequest(message.chatId(), message.text(), message.parseMode());

        try {
            SendMessageResponse response = webClient.post()
                    .uri("sendMessage")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(SendMessageResponse.class)
                    .retryWhen(buildRetry())
                    .timeout(properties.readTimeout())
                    .block();

            if (response == null || !response.ok()) {
                String desc = response != null ? response.description() : "No response";
                throw new ExternalServiceException(SERVICE_NAME, "Failed to send message: " + desc);
            }
            return true;
        } catch (ExternalServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException(SERVICE_NAME, "Failed to send message: " + e.getMessage(), e);
        }
    }

    @Override
    public TelegramBotInfo getMe() {
        try {
            GetMeResponse response = webClient.get()
                    .uri("getMe")
                    .retrieve()
                    .bodyToMono(GetMeResponse.class)
                    .retryWhen(buildRetry())
                    .timeout(properties.readTimeout())
                    .block();

            if (response == null || !response.ok() || response.result() == null) {
                throw new ExternalServiceException(SERVICE_NAME, "Failed to get bot info");
            }
            return new TelegramBotInfo(response.result().id(), response.result().username());
        } catch (ExternalServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException(SERVICE_NAME, "Failed to get bot info: " + e.getMessage(), e);
        }
    }

    private Retry buildRetry() {
        return Retry.backoff(
                properties.retry().maxAttempts(),
                Duration.ofMillis(properties.retry().initialBackoff().toMillis())
        )
                .maxBackoff(Duration.ofMillis(properties.retry().maxBackoff().toMillis()))
                .jitter(0.5)
                .doBeforeRetry(signal -> log.warn("Retrying Telegram API call after error: {}",
                        signal.failure().getMessage()));
    }
}
