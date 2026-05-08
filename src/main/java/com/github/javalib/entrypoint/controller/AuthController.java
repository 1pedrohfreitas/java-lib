package com.github.javalib.entrypoint.controller;

import com.github.javalib.application.service.GoogleAuthApplicationService;
import com.github.javalib.domain.model.GoogleUserInfo;
import com.github.javalib.dto.ValidateTokenRequest;
import com.github.javalib.entrypoint.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de autenticacao Google OAuth2.
 * Disponivel apenas quando o modulo Google OAuth esta configurado.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@ConditionalOnBean(GoogleAuthApplicationService.class)
public class AuthController {

    private final GoogleAuthApplicationService authService;

    @PostMapping("/validate")
    public ApiResponse<GoogleUserInfo> validate(@Valid @RequestBody ValidateTokenRequest request) {
        GoogleUserInfo userInfo = authService.validateToken(request.token());
        return ApiResponse.ok(userInfo);
    }
}
