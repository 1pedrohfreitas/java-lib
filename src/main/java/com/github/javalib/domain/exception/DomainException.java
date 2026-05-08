package com.github.javalib.domain.exception;

/**
 * Violacao de regra de negocio no dominio.
 */
public non-sealed class DomainException extends JavaLibException {

    public DomainException(String message) {
        super("DOMAIN_ERROR", message);
    }

    public DomainException(String message, Throwable cause) {
        super("DOMAIN_ERROR", message, cause);
    }
}
