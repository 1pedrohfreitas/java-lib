package dev.pedrohfreitas.javalib.application.service;

import dev.pedrohfreitas.javalib.domain.model.GoogleUserInfo;
import dev.pedrohfreitas.javalib.domain.port.GoogleOAuthPort;
import dev.pedrohfreitas.javalib.services.GoogleAuthDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso de validacao de token Google OAuth2.
 */
@Slf4j
@RequiredArgsConstructor
public class GoogleAuthApplicationService {

    private final GoogleAuthDomainService domainService;
    private final GoogleOAuthPort oauthPort;

    /**
     * Valida um token e retorna informacoes do usuario autenticado.
     *
     * @param token token a ser validado
     * @return informacoes do usuario
     */
    public GoogleUserInfo validateToken(String token) {
        log.info("Validating Google OAuth token");
        GoogleUserInfo userInfo = oauthPort.validateToken(token);
        return domainService.validateTokenInfo(userInfo);
    }
}
