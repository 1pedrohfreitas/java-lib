package com.github.javalib.infrastructure.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa um usuario do Telegram.
 */
public record User(
        Long id,
        @JsonProperty("is_bot") boolean isBot,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String username
) {}
