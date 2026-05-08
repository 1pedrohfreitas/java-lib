package com.github.javalib.domain.model;

/**
 * Informacoes do bot obtidas via getMe.
 */
public record TelegramBotInfo(
        Long id,
        String username
) {}
