package dev.pedrohfreitas.javalib.domain.exception;

/**
 * Raiz selada da hierarquia de excecoes de dominio.
 */
public abstract sealed class JavaLibException extends RuntimeException
        permits DomainException,
                NotFoundException,
                UnauthorizedException,
                BadRequestException,
                ConflictException,
                ExternalServiceException {

    private final String errorCode;

    protected JavaLibException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected JavaLibException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
