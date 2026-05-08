package com.github.javalib.entrypoint.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String errorCode,
        String message,
        List<FieldError> details,
        Instant timestamp
) {
    public record FieldError(String field, String message) {}

    public static ErrorResponse of(String errorCode, String message) {
        return new ErrorResponse(errorCode, message, null, Instant.now());
    }

    public static ErrorResponse withDetails(String errorCode, String message, List<FieldError> details) {
        return new ErrorResponse(errorCode, message, details, Instant.now());
    }
}
