package dev.pedrohfreitas.javalib.entrypoint;

import dev.pedrohfreitas.javalib.application.service.GoogleAuthApplicationService;
import dev.pedrohfreitas.javalib.domain.model.GoogleUserInfo;
import dev.pedrohfreitas.javalib.entrypoint.controller.AuthController;
import dev.pedrohfreitas.javalib.entrypoint.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private MockMvc mockMvc;
    private GoogleAuthApplicationService authService;

    @BeforeEach
    void setUp() {
        authService = mock(GoogleAuthApplicationService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(authService))
                .setValidator(new LocalValidatorFactoryBean())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("should validate token successfully")
    void shouldValidateTokenSuccessfully() throws Exception {
        var userInfo = new GoogleUserInfo("sub123", "user@example.com", "John", "https://pic.jpg", true);
        when(authService.validateToken("valid-token")).thenReturn(userInfo);

        mockMvc.perform(post("/api/v1/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"token":"valid-token"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("user@example.com"));
    }

    @Test
    @DisplayName("should return bad request when token is blank")
    void shouldReturnBadRequestWhenTokenIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"token":""}
                                """))
                .andExpect(status().isBadRequest());
    }
}
