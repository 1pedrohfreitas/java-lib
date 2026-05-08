package dev.pedrohfreitas.javalib.domain.exception;

/**
 * Requisicao invalida ou malformada.
 */
public non-sealed class BadRequestException extends JavaLibException {

    public BadRequestException(String message) {
        super("BAD_REQUEST", message);
    }
}
