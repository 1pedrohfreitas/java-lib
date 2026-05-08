package com.github.javalib.application.service;

import com.github.javalib.domain.model.GoogleUserInfo;
import com.github.javalib.domain.port.GoogleOAuthPort;
import com.github.javalib.services.GoogleAuthDomainService;
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
