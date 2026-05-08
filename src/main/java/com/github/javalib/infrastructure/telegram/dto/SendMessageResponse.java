package com.github.javalib.infrastructure.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response body do endpoint sendMessage do Telegram.
 */
public record SendMessageResponse(
        boolean ok,
        Message result,
        @JsonProperty("error_code") Integer errorCode,
        String description
) {}
