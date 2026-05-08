package com.github.javalib.domain.exception;

/**
 * Conflito de estado, como recurso duplicado.
 */
public non-sealed class ConflictException extends JavaLibException {

    public ConflictException(String message) {
        super("CONFLICT", message);
    }
}
