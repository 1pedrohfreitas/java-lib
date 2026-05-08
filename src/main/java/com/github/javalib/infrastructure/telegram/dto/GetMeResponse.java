package com.github.javalib.infrastructure.telegram.dto;

/**
 * Response body do endpoint getMe do Telegram.
 */
public record GetMeResponse(
        boolean ok,
        User result
) {}
