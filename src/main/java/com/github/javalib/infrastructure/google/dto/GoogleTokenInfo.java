package com.github.javalib.infrastructure.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response do endpoint tokeninfo do Google.
 */
public record GoogleTokenInfo(
        String iss,
        String azp,
        String aud,
        String sub,
        String email,
        @JsonProperty("email_verified") Boolean emailVerified,
        String exp,
        String iat,
        String name,
        String picture
) {}
