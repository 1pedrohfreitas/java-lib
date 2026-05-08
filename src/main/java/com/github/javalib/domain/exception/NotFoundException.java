package com.github.javalib.domain.exception;

/**
 * Recurso nao encontrado.
 */
public non-sealed class NotFoundException extends JavaLibException {

    public NotFoundException(String resource, String id) {
        super("NOT_FOUND", "%s not found with id: %s".formatted(resource, id));
    }
}
