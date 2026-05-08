package com.github.javalib.entrypoint;

import com.github.javalib.domain.exception.*;
import com.github.javalib.entrypoint.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/not-found")
        void notFound() {
            throw new NotFoundException("User", "123");
        }

        @GetMapping("/unauthorized")
        void unauthorized() {
            throw new UnauthorizedException("Invalid token");
        }

        @GetMapping("/bad-request")
        void badRequest() {
            throw new BadRequestException("Missing field");
        }

        @GetMapping("/conflict")
        void conflict() {
            throw new ConflictException("Duplicate resource");
        }

        @GetMapping("/external-service")
        void externalService() {
            throw new ExternalServiceException("Telegram", "Connection timeout");
        }

        @GetMapping("/domain-error")
        void domainError() {
            throw new DomainException("Business rule violated");
        }

        @GetMapping("/server-error")
        void serverError() {
            throw new RuntimeException("Unexpected error");
        }

        @GetMapping("/ok")
        String ok() {
            return "ok";
        }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("should return 404 for NotFoundException")
    void shouldReturn404ForNotFoundException() throws Exception {
        mockMvc.perform(get("/test/not-found")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    @DisplayName("should return 401 for UnauthorizedException")
    void shouldReturn401ForUnauthorizedException() throws Exception {
        mockMvc.perform(get("/test/unauthorized")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    @Test
    @DisplayName("should return 400 for BadRequestException")
    void shouldReturn400ForBadRequestException() throws Exception {
        mockMvc.perform(get("/test/bad-request")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("should return 409 for ConflictException")
    void shouldReturn409ForConflictException() throws Exception {
        mockMvc.perform(get("/test/conflict")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("CONFLICT"));
    }

    @Test
    @DisplayName("should return 502 for ExternalServiceException")
    void shouldReturn502ForExternalServiceException() throws Exception {
        mockMvc.perform(get("/test/external-service")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.errorCode").value("EXTERNAL_SERVICE_ERROR"));
    }

    @Test
    @DisplayName("should return 422 for DomainException")
    void shouldReturn422ForDomainException() throws Exception {
        mockMvc.perform(get("/test/domain-error")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value("DOMAIN_ERROR"));
    }

    @Test
    @DisplayName("should return 500 for generic Exception")
    void shouldReturn500ForGenericException() throws Exception {
        mockMvc.perform(get("/test/server-error")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_ERROR"));
    }

    @Test
    @DisplayName("should not interfere with successful responses")
    void shouldNotInterfereWithSuccessfulResponses() throws Exception {
        mockMvc.perform(get("/test/ok")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
