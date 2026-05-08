package dev.pedrohfreitas.javalib.domain.exception;

/**
 * Falha em servico externo (Telegram, Google, etc.).
 */
public non-sealed class ExternalServiceException extends JavaLibException {

    private final String serviceName;

    public ExternalServiceException(String serviceName, String message) {
        super("EXTERNAL_SERVICE_ERROR", "[%s] %s".formatted(serviceName, message));
        this.serviceName = serviceName;
    }

    public ExternalServiceException(String serviceName, String message, Throwable cause) {
        super("EXTERNAL_SERVICE_ERROR", "[%s] %s".formatted(serviceName, message), cause);
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
