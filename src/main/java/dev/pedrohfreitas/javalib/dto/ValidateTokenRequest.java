package dev.pedrohfreitas.javalib.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request para validacao de token OAuth2.
 */
public record ValidateTokenRequest(
        @NotBlank String token
) {}
