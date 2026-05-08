package dev.pedrohfreitas.javalib.infrastructure.google.config;

import dev.pedrohfreitas.javalib.infrastructure.google.GoogleOAuth2Client;
import dev.pedrohfreitas.javalib.infrastructure.google.GoogleOAuth2Properties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Auto-configuracao do cliente Google OAuth2.
 * Ativada apenas quando java-lib.security.google.expected-audience esta configurado.
 */
@AutoConfiguration
@ConditionalOnProperty("java-lib.security.google.expected-audience")
@EnableConfigurationProperties(GoogleOAuth2Properties.class)
public class GoogleOAuth2Config {

    @Bean
    public GoogleOAuth2Client googleOAuth2Client(GoogleOAuth2Properties props, WebClient.Builder builder) {
        WebClient webClient = builder
                .baseUrl(props.tokenInfoUrl())
                .build();

        return new GoogleOAuth2Client(webClient, props);
    }
}
