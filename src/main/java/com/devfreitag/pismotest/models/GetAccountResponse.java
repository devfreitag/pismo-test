package com.devfreitag.pismotest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing account details")
public record GetAccountResponse(
        @Schema(description = "Unique identifier for the account", example = "1")
        @JsonProperty("account_id")
        Long accountId,

        @Schema(description = "Document number of the account holder", example = "12345678900")
        @JsonProperty("document_number")
        String documentNumber
) {}
