package com.github.javalib.infrastructure.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request body para o endpoint sendMessage do Telegram.
 */
public record SendMessageRequest(
        @JsonProperty("chat_id") String chatId,
        String text,
        @JsonProperty("parse_mode") String parseMode
) {
    public SendMessageRequest(String chatId, String text) {
        this(chatId, text, null);
    }
}
