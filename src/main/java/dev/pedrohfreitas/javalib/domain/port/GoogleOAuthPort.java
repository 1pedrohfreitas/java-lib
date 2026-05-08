package dev.pedrohfreitas.javalib.domain.port;

import dev.pedrohfreitas.javalib.domain.model.GoogleUserInfo;

/**
 * Porta de saida para o Google OAuth2.
 */
public interface GoogleOAuthPort {

    /**
     * Valida um token OAuth2 e retorna as informacoes do usuario.
     *
     * @param accessToken token de acesso
     * @return informacoes do usuario autenticado
     */
    GoogleUserInfo validateToken(String accessToken);
}
