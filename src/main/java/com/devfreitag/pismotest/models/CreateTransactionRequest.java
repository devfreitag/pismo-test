package com.devfreitag.pismotest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateTransactionRequest(
        @JsonProperty("account_id")
        @NotNull(message = "Account must be informed")
        Long accountId,

        @JsonProperty("operation_type_id")
        @NotNull(message = "Operation type must be informed")
        Long operationTypeId,

        @JsonProperty("amount")
        @NotNull(message = "Amount must be informed")
        @Positive
        BigDecimal amount) {
}
