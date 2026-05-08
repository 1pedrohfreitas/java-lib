package com.github.javalib.domain.exception;

/**
 * Falha de autenticacao ou autorizacao.
 */
public non-sealed class UnauthorizedException extends JavaLibException {

    public UnauthorizedException(String message) {
        super("UNAUTHORIZED", message);
    }
}
