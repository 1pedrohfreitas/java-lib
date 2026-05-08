package dev.pedrohfreitas.javalib.infrastructure.telegram;

import com.sun.net.httpserver.HttpServer;
import dev.pedrohfreitas.javalib.domain.exception.ExternalServiceException;
import dev.pedrohfreitas.javalib.domain.model.TelegramMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TelegramBotClientTest {

    private static HttpServer server;
    private static int port;

    @BeforeAll
    static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.setExecutor(Executors.newFixedThreadPool(2));

        server.createContext("/bottest-token/sendMessage", exchange -> {
            byte[] body = exchange.getRequestBody().readAllBytes();
            String response;
            if (body.length > 0 && new String(body).contains("\"fail\"")) {
                response = "{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: chat not found\"}";
            } else {
                response = "{\"ok\":true,\"result\":{\"message_id\":1,\"chat\":{\"id\":123,\"type\":\"private\"},\"from\":{\"id\":456,\"is_bot\":true,\"first_name\":\"TestBot\",\"username\":\"test_bot\"},\"text\":\"Hello\",\"date\":1700000000}}";
            }
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });

        server.createContext("/bottest-token/getMe", exchange -> {
            String response = "{\"ok\":true,\"result\":{\"id\":456,\"is_bot\":true,\"first_name\":\"TestBot\",\"username\":\"test_bot\"}}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });

        server.start();
        port = server.getAddress().getPort();
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    private TelegramBotClient createClient() {
        String apiUrl = "http://localhost:" + port + "/bot";
        var props = new TelegramBotProperties(
                "test-token",
                apiUrl,
                new TelegramBotProperties.Retry(1, Duration.ofMillis(10), Duration.ofMillis(50)),
                Duration.ofSeconds(10),
                Duration.ofSeconds(10)
        );

        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl + props.botToken() + "/")
                .build();

        return new TelegramBotClient(webClient, props);
    }

    @Test
    @DisplayName("should send message successfully")
    void shouldSendMessageSuccessfully() {
        var client = createClient();
        var message = new TelegramMessage("123", "Hello");

        boolean result = client.sendMessage(message);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should throw ExternalServiceException on Telegram error response")
    void shouldThrowOnErrorResponse() {
        var client = createClient();
        var message = new TelegramMessage("123", "fail");

        assertThatThrownBy(() -> client.sendMessage(message))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("Telegram")
                .hasMessageContaining("chat not found");
    }

    @Test
    @DisplayName("should retrieve bot info successfully")
    void shouldRetrieveBotInfo() {
        var client = createClient();
        var botInfo = client.getMe();

        assertThat(botInfo.id()).isEqualTo(456);
        assertThat(botInfo.username()).isEqualTo("test_bot");
    }

    @Test
    @DisplayName("should create client programmatically via factory method")
    void shouldCreateClientViaFactoryMethod() {
        String apiUrl = "http://localhost:" + port + "/bot";
        var props = new TelegramBotProperties(
                "factory-token",
                apiUrl,
                new TelegramBotProperties.Retry(1, Duration.ofMillis(10), Duration.ofMillis(50)),
                Duration.ofSeconds(10),
                Duration.ofSeconds(10)
        );

        var client = TelegramBotClient.create(props);

        assertThat(client).isNotNull();
    }
}
