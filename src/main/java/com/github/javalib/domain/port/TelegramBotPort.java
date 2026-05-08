package com.github.javalib.domain.port;

import com.github.javalib.domain.model.TelegramBotInfo;
import com.github.javalib.domain.model.TelegramMessage;

/**
 * Porta de saida para o Telegram Bot API.
 */
public interface TelegramBotPort {

    /**
     * Envia uma mensagem para um chat.
     *
     * @param message mensagem a ser enviada
     * @return true se enviada com sucesso
     */
    boolean sendMessage(TelegramMessage message);

    /**
     * Obtem informacoes do bot autenticado.
     *
     * @return informacoes do bot
     */
    TelegramBotInfo getMe();
}
