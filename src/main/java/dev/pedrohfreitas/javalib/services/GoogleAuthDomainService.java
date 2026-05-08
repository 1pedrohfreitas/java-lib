package dev.pedrohfreitas.javalib.services;

import dev.pedrohfreitas.javalib.domain.exception.UnauthorizedException;
import dev.pedrohfreitas.javalib.domain.model.GoogleUserInfo;

import java.util.List;

/**
 * Regras de negocio para validacao de tokens Google OAuth2.
 */
public class GoogleAuthDomainService {

    private final List<String> allowedIssuers;
    private final String expectedAudience;

    public GoogleAuthDomainService(List<String> allowedIssuers, String expectedAudience) {
        this.allowedIssuers = allowedIssuers;
        this.expectedAudience = expectedAudience;
    }

    /**
     * Valida informacoes de usuario retornadas pelo Google.
     *
     * @param userInfo informacoes do usuario
     * @return informacoes validadas
     */
    public GoogleUserInfo validateTokenInfo(GoogleUserInfo userInfo) {
        if (!userInfo.emailVerified()) {
            throw new UnauthorizedException(
                    "User email is not verified: %s".formatted(userInfo.email())
            );
        }
        return userInfo;
    }
}
