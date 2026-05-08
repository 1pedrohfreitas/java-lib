package dev.pedrohfreitas.javalib.domain.port;

import dev.pedrohfreitas.javalib.domain.model.TelegramBotInfo;
import dev.pedrohfreitas.javalib.domain.model.TelegramMessage;

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
