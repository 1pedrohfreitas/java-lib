package dev.pedrohfreitas.javalib.domain.model;

import java.time.Instant;

/**
 * Token OAuth2 validado.
 */
public record OAuthToken(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        String scope
) {}
