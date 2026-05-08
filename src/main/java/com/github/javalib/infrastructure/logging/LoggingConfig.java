package com.github.javalib.infrastructure.logging;

import jakarta.servlet.Filter;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * Configuracao de logging estruturado com correlation ID.
 */
@Configuration
public class LoggingConfig {

    @Bean
    public Filter correlationIdFilter() {
        return (request, response, chain) -> {
            String correlationId = UUID.randomUUID().toString();
            MDC.put("correlationId", correlationId);
            try {
                chain.doFilter(request, response);
            } finally {
                MDC.remove("correlationId");
            }
        };
    }
}
