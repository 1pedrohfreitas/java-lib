package com.github.javalib.services;

import com.github.javalib.domain.exception.BadRequestException;
import com.github.javalib.domain.model.TelegramMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Regras de negocio para envio de mensagens Telegram.
 */
public class TelegramDomainService {

    private static final int MAX_MESSAGE_LENGTH = 4096;

    /**
     * Valida se a mensagem pode ser enviada.
     *
     * @param message mensagem a validar
     * @return a mensagem validada
     */
    public TelegramMessage validateMessage(TelegramMessage message) {
        if (message == null) {
            throw new BadRequestException("Telegram message must not be null");
        }
        return message;
    }

    /**
     * Divide mensagem longa em chunks de ate 4096 caracteres.
     *
     * @param message mensagem original
     * @return lista de mensagens com texto dividido
     */
    public List<TelegramMessage> splitIfNeeded(TelegramMessage message) {
        String text = message.text();
        if (text.length() <= MAX_MESSAGE_LENGTH) {
            return List.of(message);
        }

        List<TelegramMessage> chunks = new ArrayList<>();
        for (int i = 0; i < text.length(); i += MAX_MESSAGE_LENGTH) {
            int end = Math.min(i + MAX_MESSAGE_LENGTH, text.length());
            chunks.add(new TelegramMessage(message.chatId(), text.substring(i, end), message.parseMode()));
        }
        return chunks;
    }
}
