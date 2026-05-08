package com.github.javalib.infrastructure.google;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Propriedades de configuracao do Google OAuth2.
 */
@ConfigurationProperties(prefix = "java-lib.security.google")
public record GoogleOAuth2Properties(
        List<String> allowedIssuers,
        String expectedAudience,
        String userInfoUrl,
        String tokenInfoUrl
) {}
