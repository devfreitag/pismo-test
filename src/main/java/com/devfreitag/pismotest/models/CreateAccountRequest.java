package com.devfreitag.pismotest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for creating a new account")
public record CreateAccountRequest(
        @Schema(description = "Unique document number identifying the account holder (e.g., CPF, CNPJ)", example = "12345678900")
        @JsonProperty("document_number")
        @NotBlank(message = "Document number is required")
        String documentNumber
) {}
