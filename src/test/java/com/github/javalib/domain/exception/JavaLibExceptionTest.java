package com.github.javalib.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JavaLibExceptionTest {

    @Test
    @DisplayName("should have all permitted subclasses")
    void shouldHaveAllPermittedSubclasses() {
        Class<?>[] permitted = JavaLibException.class.getPermittedSubclasses();

        assertThat(permitted)
                .extracting(Class::getSimpleName)
                .containsExactlyInAnyOrder(
                        "DomainException",
                        "NotFoundException",
                        "UnauthorizedException",
                        "BadRequestException",
                        "ConflictException",
                        "ExternalServiceException"
                );
    }

    @Test
    @DisplayName("should allow catching by base type")
    void shouldAllowCatchingByBaseType() {
        var ex = new NotFoundException("User", "123");

        assertThat(ex).isInstanceOf(JavaLibException.class);
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("NotFoundException should contain resource info")
    void notFoundExceptionShouldContainResourceInfo() {
        var ex = new NotFoundException("User", "abc-123");

        assertThat(ex.getMessage()).contains("User").contains("abc-123");
        assertThat(ex.getErrorCode()).isEqualTo("NOT_FOUND");
    }

    @Test
    @DisplayName("UnauthorizedException should carry error code")
    void unauthorizedExceptionShouldCarryErrorCode() {
        var ex = new UnauthorizedException("Invalid token");

        assertThat(ex.getErrorCode()).isEqualTo("UNAUTHORIZED");
        assertThat(ex.getMessage()).isEqualTo("Invalid token");
    }

    @Test
    @DisplayName("BadRequestException should carry error code")
    void badRequestExceptionShouldCarryErrorCode() {
        var ex = new BadRequestException("Missing required field");

        assertThat(ex.getErrorCode()).isEqualTo("BAD_REQUEST");
        assertThat(ex.getMessage()).isEqualTo("Missing required field");
    }

    @Test
    @DisplayName("ConflictException should carry error code")
    void conflictExceptionShouldCarryErrorCode() {
        var ex = new ConflictException("Resource already exists");

        assertThat(ex.getErrorCode()).isEqualTo("CONFLICT");
    }

    @Test
    @DisplayName("ExternalServiceException should identify service name")
    void externalServiceExceptionShouldIdentifyService() {
        var ex = new ExternalServiceException("Telegram", "Timeout after 30s");

        assertThat(ex.getErrorCode()).isEqualTo("EXTERNAL_SERVICE_ERROR");
        assertThat(ex.getServiceName()).isEqualTo("Telegram");
        assertThat(ex.getMessage()).contains("Timeout after 30s");
    }

    @Test
    @DisplayName("ExternalServiceException should wrap cause")
    void externalServiceExceptionShouldWrapCause() {
        var cause = new RuntimeException("Connection refused");
        var ex = new ExternalServiceException("Telegram", "Request failed", cause);

        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("DomainException should carry error code")
    void domainExceptionShouldCarryErrorCode() {
        var ex = new DomainException("Business rule violation");

        assertThat(ex.getErrorCode()).isEqualTo("DOMAIN_ERROR");
    }

    @Test
    @DisplayName("exception should be throwable and catchable")
    void exceptionShouldBeThrowableAndCatchable() {
        assertThatThrownBy(() -> {
            throw new NotFoundException("Order", "42");
        })
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Order")
                .hasMessageContaining("42");
    }
}
