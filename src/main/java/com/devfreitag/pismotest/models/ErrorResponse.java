package com.devfreitag.pismotest.models;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Standard error response for API errors")
public record ErrorResponse(
        @Schema(description = "Error message describing what went wrong", example = "Account not found")
        String message,

        @Schema(description = "Map of field-specific validation errors (field name -> error message)")
        Map<String, String> errors
) {}
