package dev.pedrohfreitas.javalib.infrastructure.telegram.dto;

/**
 * Representa um chat do Telegram.
 */
public record Chat(
        Long id,
        String type,
        String title,
        String username
) {}
