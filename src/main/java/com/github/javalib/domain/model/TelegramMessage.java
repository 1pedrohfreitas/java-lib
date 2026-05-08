package com.github.javalib.domain.model;

/**
 * Mensagem a ser enviada via Telegram Bot API.
 */
public record TelegramMessage(
        String chatId,
        String text,
        String parseMode
) {
    public TelegramMessage {
        if (chatId == null || chatId.isBlank()) {
            throw new IllegalArgumentException("chatId must not be blank");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("text must not be blank");
        }
    }

    public TelegramMessage(String chatId, String text) {
        this(chatId, text, null);
    }
}
