package com.github.javalib.infrastructure.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa uma mensagem do Telegram.
 */
public record Message(
        @JsonProperty("message_id") Long messageId,
        Chat chat,
        User from,
        String text,
        Long date
) {}
