package com.github.javalib.services;

import com.github.javalib.domain.exception.BadRequestException;
import com.github.javalib.domain.model.TelegramMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TelegramDomainServiceTest {

    private final TelegramDomainService service = new TelegramDomainService();

    @Test
    @DisplayName("should validate message successfully")
    void shouldValidateMessageSuccessfully() {
        var message = new TelegramMessage("123456", "Hello");

        assertThat(service.validateMessage(message)).isEqualTo(message);
    }

    @Test
    @DisplayName("should throw when message is null")
    void shouldThrowWhenMessageIsNull() {
        assertThatThrownBy(() -> service.validateMessage(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("null");
    }

    @Test
    @DisplayName("should split message longer than 4096 characters")
    void shouldSplitMessageLongerThan4096Chars() {
        var longText = "A".repeat(5000);
        var message = new TelegramMessage("123456", longText);

        List<TelegramMessage> chunks = service.splitIfNeeded(message);

        assertThat(chunks).hasSize(2);
        assertThat(chunks.get(0).text()).hasSize(4096);
        assertThat(chunks.get(1).text()).hasSize(5000 - 4096);
    }

    @Test
    @DisplayName("should not split message shorter than 4096 characters")
    void shouldNotSplitShortMessage() {
        var message = new TelegramMessage("123456", "Hello");

        List<TelegramMessage> chunks = service.splitIfNeeded(message);

        assertThat(chunks).hasSize(1);
        assertThat(chunks.get(0)).isEqualTo(message);
    }
}
