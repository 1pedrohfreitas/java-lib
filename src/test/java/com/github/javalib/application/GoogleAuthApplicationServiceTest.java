package com.github.javalib.application;

import com.github.javalib.application.service.GoogleAuthApplicationService;
import com.github.javalib.domain.model.GoogleUserInfo;
import com.github.javalib.domain.port.GoogleOAuthPort;
import com.github.javalib.services.GoogleAuthDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleAuthApplicationServiceTest {

    @Mock
    private GoogleOAuthPort oauthPort;

    private GoogleAuthApplicationService service;

    @BeforeEach
    void setUp() {
        var domainService = new GoogleAuthDomainService(
                List.of("https://accounts.google.com"), "test-client-id"
        );
        service = new GoogleAuthApplicationService(domainService, oauthPort);
    }

    @Test
    @DisplayName("should validate token successfully")
    void shouldValidateTokenSuccessfully() {
        var userInfo = new GoogleUserInfo("sub123", "user@example.com", "John", "https://pic.jpg", true);
        when(oauthPort.validateToken("valid-token")).thenReturn(userInfo);

        GoogleUserInfo result = service.validateToken("valid-token");

        assertThat(result).isEqualTo(userInfo);
        verify(oauthPort).validateToken("valid-token");
    }
}
