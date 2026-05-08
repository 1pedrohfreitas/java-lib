package com.github.javalib.infrastructure.telegram;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TelegramBotPropertiesTest.TestConfig.class)
@ActiveProfiles("test")
class TelegramBotPropertiesTest {

    @org.springframework.boot.test.context.TestConfiguration
    @EnableConfigurationProperties(TelegramBotProperties.class)
    static class TestConfig {}

    @Autowired
    private TelegramBotProperties props;

    @Test
    @DisplayName("should bind telegram properties from test profile")
    void shouldBindTelegramProperties() {
        assertThat(props.botToken()).isEqualTo("test-bot-token");
        assertThat(props.apiUrl()).isNotNull();
        assertThat(props.retry().maxAttempts()).isEqualTo(1);
        assertThat(props.retry().initialBackoff()).isEqualTo(Duration.ofMillis(10));
        assertThat(props.connectTimeout()).isEqualTo(Duration.ofSeconds(2));
        assertThat(props.readTimeout()).isEqualTo(Duration.ofSeconds(2));
    }
}
