package com.github.javalib.domain.model;

/**
 * Informacoes do usuario Google autenticado.
 */
public record GoogleUserInfo(
        String sub,
        String email,
        String name,
        String picture,
        boolean emailVerified
) {}
