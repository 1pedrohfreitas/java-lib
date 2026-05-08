package dev.pedrohfreitas.javalib.infrastructure.telegram;

import dev.pedrohfreitas.javalib.domain.exception.ExternalServiceException;
import dev.pedrohfreitas.javalib.domain.model.TelegramMessage;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Disabled("Temporarily disabled due to WireMock timeout issues")
class TelegramBotClientIT {

    @RegisterExtension
    WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .failOnUnmatchedRequests(false)
            .build();

    private TelegramBotClient createClient() {
        String apiUrl = "http://localhost:" + wireMock.getPort() + "/bot";
        var props = new TelegramBotProperties(
                "test-token",
                apiUrl,
                new TelegramBotProperties.Retry(1, Duration.ofMillis(10), Duration.ofMillis(50)),
                Duration.ofSeconds(5),
                Duration.ofSeconds(5)
        );

        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl + props.botToken() + "/")
                .build();

        return new TelegramBotClient(webClient, props);
    }

    @Test
    @DisplayName("should send message successfully")
    void shouldSendMessageSuccessfully() {
        wireMock.stubFor(post(urlEqualTo("/bottest-token/sendMessage"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"ok":true,"result":{"message_id":1,"chat":{"id":123,"type":"private"},"from":{"id":456,"is_bot":true,"first_name":"TestBot","username":"test_bot"},"text":"Hello","date":1700000000}}
                                """)));

        var client = createClient();
        var message = new TelegramMessage("123", "Hello");

        boolean result = client.sendMessage(message);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should throw ExternalServiceException on Telegram error response")
    void shouldThrowOnErrorResponse() {
        wireMock.stubFor(post(urlEqualTo("/bottest-token/sendMessage"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"ok":false,"error_code":400,"description":"Bad Request: chat not found"}
                                """)));

        var client = createClient();
        var message = new TelegramMessage("123", "Hello");

        assertThatThrownBy(() -> client.sendMessage(message))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("Telegram")
                .hasMessageContaining("chat not found");
    }

    @Test
    @DisplayName("should retrieve bot info successfully")
    void shouldRetrieveBotInfo() {
        wireMock.stubFor(get(urlEqualTo("/bottest-token/getMe"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"ok":true,"result":{"id":456,"is_bot":true,"first_name":"TestBot","username":"test_bot"}}
                                """)));

        var client = createClient();
        var botInfo = client.getMe();

        assertThat(botInfo.id()).isEqualTo(456);
        assertThat(botInfo.username()).isEqualTo("test_bot");
    }

    @Test
    @DisplayName("should throw ExternalServiceException on server error")
    void shouldThrowOnServerError() {
        wireMock.stubFor(post(urlEqualTo("/bottest-token/sendMessage"))
                .willReturn(aResponse().withStatus(500)));

        var client = createClient();
        var message = new TelegramMessage("123", "Hello");

        assertThatThrownBy(() -> client.sendMessage(message))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("Telegram");
    }

    @Test
    @DisplayName("should create client programmatically via factory method")
    void shouldCreateClientViaFactoryMethod() {
        String apiUrl = "http://localhost:" + wireMock.getPort() + "/bot";
        var props = new TelegramBotProperties(
                "factory-token",
                apiUrl,
                new TelegramBotProperties.Retry(1, Duration.ofMillis(10), Duration.ofMillis(50)),
                Duration.ofSeconds(5),
                Duration.ofSeconds(5)
        );

        wireMock.stubFor(get(urlEqualTo("/botfactory-token/getMe"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"ok":true,"result":{"id":789,"is_bot":true,"first_name":"FactoryBot","username":"factory_bot"}}
                                """)));

        var client = TelegramBotClient.create(props);

        var botInfo = client.getMe();
        assertThat(botInfo.id()).isEqualTo(789);
        assertThat(botInfo.username()).isEqualTo("factory_bot");
    }
}
