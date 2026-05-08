package dev.pedrohfreitas.javalib.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request para envio de mensagem via bot.
 */
public record SendMessageRequest(
        @NotBlank String chatId,
        @NotBlank String text,
        String parseMode
) {}
