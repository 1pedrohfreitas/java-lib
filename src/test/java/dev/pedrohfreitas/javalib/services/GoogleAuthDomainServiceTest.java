package dev.pedrohfreitas.javalib.services;

import dev.pedrohfreitas.javalib.domain.exception.UnauthorizedException;
import dev.pedrohfreitas.javalib.domain.model.GoogleUserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GoogleAuthDomainServiceTest {

    private static final List<String> ALLOWED_ISSUERS = List.of(
            "https://accounts.google.com",
            "accounts.google.com"
    );
    private static final String EXPECTED_AUDIENCE = "test-client-id";

    private final GoogleAuthDomainService service = new GoogleAuthDomainService(
            ALLOWED_ISSUERS, EXPECTED_AUDIENCE
    );

    @Test
    @DisplayName("should validate a valid token response")
    void shouldValidateValidTokenResponse() {
        GoogleUserInfo userInfo = new GoogleUserInfo(
                "sub123", "user@example.com", "John", "https://pic.jpg", true
        );

        GoogleUserInfo result = service.validateTokenInfo(userInfo);

        assertThat(result).isEqualTo(userInfo);
    }

    @Test
    @DisplayName("should throw when email is not verified")
    void shouldThrowWhenEmailNotVerified() {
        GoogleUserInfo userInfo = new GoogleUserInfo(
                "sub123", "user@example.com", "John", "https://pic.jpg", false
        );

        assertThatThrownBy(() -> service.validateTokenInfo(userInfo))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("email")
                .hasMessageContaining("not verified");
    }
}
