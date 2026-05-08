package dev.pedrohfreitas.javalib.domain.model;

/**
 * Informacoes do bot obtidas via getMe.
 */
public record TelegramBotInfo(
        Long id,
        String username
) {}
