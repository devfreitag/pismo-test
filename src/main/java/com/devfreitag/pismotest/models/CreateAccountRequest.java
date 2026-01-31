package com.devfreitag.pismotest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @JsonProperty("document_number")
        @NotBlank(message = "Document number is required")
        String documentNumber
) {}
