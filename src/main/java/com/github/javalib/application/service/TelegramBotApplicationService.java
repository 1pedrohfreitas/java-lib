package com.github.javalib.application.service;

import com.github.javalib.domain.model.TelegramBotInfo;
import com.github.javalib.domain.model.TelegramMessage;
import com.github.javalib.domain.port.TelegramBotPort;
import com.github.javalib.services.TelegramDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso de envio de mensagens via Telegram.
 */
@Slf4j
@RequiredArgsConstructor
public class TelegramBotApplicationService {

    private final TelegramDomainService domainService;
    private final TelegramBotPort botPort;

    /**
     * Envia uma mensagem, dividindo automaticamente se exceder 4096 caracteres.
     *
     * @param message mensagem a enviar
     * @return true se todas as partes forem enviadas
     */
    public boolean sendMessage(TelegramMessage message) {
        var validated = domainService.validateMessage(message);
        var chunks = domainService.splitIfNeeded(validated);

        if (chunks.size() > 1) {
            log.info("Message split into {} chunks for chat {}", chunks.size(), message.chatId());
        }

        for (var chunk : chunks) {
            botPort.sendMessage(chunk);
        }
        return true;
    }

    /**
     * Obtem informacoes do bot.
     *
     * @return informacoes do bot
     */
    public TelegramBotInfo getBotInfo() {
        return botPort.getMe();
    }
}
