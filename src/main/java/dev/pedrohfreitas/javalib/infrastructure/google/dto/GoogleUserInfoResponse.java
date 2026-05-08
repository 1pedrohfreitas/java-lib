package dev.pedrohfreitas.javalib.infrastructure.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response do endpoint userinfo do Google OAuth2.
 */
public record GoogleUserInfoResponse(
        String sub,
        String name,
        @JsonProperty("given_name") String givenName,
        @JsonProperty("family_name") String familyName,
        String picture,
        String email,
        @JsonProperty("email_verified") Boolean emailVerified,
        String locale,
        String hd
) {}
