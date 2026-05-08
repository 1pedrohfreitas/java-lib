package dev.pedrohfreitas.javalib.infrastructure.google;

import dev.pedrohfreitas.javalib.domain.exception.ExternalServiceException;
import dev.pedrohfreitas.javalib.domain.exception.UnauthorizedException;
import dev.pedrohfreitas.javalib.domain.model.GoogleUserInfo;
import dev.pedrohfreitas.javalib.domain.port.GoogleOAuthPort;
import dev.pedrohfreitas.javalib.infrastructure.google.dto.GoogleTokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Cliente HTTP para validacao de tokens Google OAuth2.
 */
@Slf4j
public class GoogleOAuth2Client implements GoogleOAuthPort {

    private static final String SERVICE_NAME = "Google OAuth";

    private final WebClient tokenInfoClient;
    private final GoogleOAuth2Properties properties;

    public GoogleOAuth2Client(WebClient tokenInfoClient, GoogleOAuth2Properties properties) {
        this.tokenInfoClient = tokenInfoClient;
        this.properties = properties;
    }

    /**
     * Cria um cliente Google OAuth2 programaticamente, sem necessidade de Spring.
     * O WebClient eh construido internamente com a URL base correta.
     */
    public static GoogleOAuth2Client create(GoogleOAuth2Properties properties) {
        WebClient webClient = WebClient.builder()
                .baseUrl(properties.tokenInfoUrl())
                .build();
        return new GoogleOAuth2Client(webClient, properties);
    }

    @Override
    public GoogleUserInfo validateToken(String accessToken) {
        try {
            GoogleTokenInfo tokenInfo = tokenInfoClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("id_token", accessToken)
                            .build())
                    .retrieve()
                    .bodyToMono(GoogleTokenInfo.class)
                    .block();

            if (tokenInfo == null) {
                throw new UnauthorizedException("Invalid token: no response from Google");
            }

            validateIssuer(tokenInfo);
            validateAudience(tokenInfo);

            return new GoogleUserInfo(
                    tokenInfo.sub(),
                    tokenInfo.email(),
                    tokenInfo.name(),
                    tokenInfo.picture(),
                    tokenInfo.emailVerified() != null && tokenInfo.emailVerified()
            );
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException(SERVICE_NAME, "Token validation failed: " + e.getMessage(), e);
        }
    }

    private void validateIssuer(GoogleTokenInfo tokenInfo) {
        if (!properties.allowedIssuers().contains(tokenInfo.iss())) {
            throw new UnauthorizedException(
                    "Invalid token issuer: %s".formatted(tokenInfo.iss())
            );
        }
    }

    private void validateAudience(GoogleTokenInfo tokenInfo) {
        if (!properties.expectedAudience().equals(tokenInfo.aud())) {
            throw new UnauthorizedException(
                    "Invalid token audience: %s".formatted(tokenInfo.aud())
            );
        }
    }
}
