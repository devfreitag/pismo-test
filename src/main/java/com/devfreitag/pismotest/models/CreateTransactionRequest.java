package com.devfreitag.pismotest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Request payload for creating a new transaction")
public record CreateTransactionRequest(
        @Schema(description = "ID of the account to associate with this transaction", example = "1")
        @JsonProperty("account_id")
        @NotNull(message = "Account must be informed")
        Long accountId,

        @Schema(
                description = "Operation type: 1=PURCHASE, 2=INSTALLMENT_PURCHASE, 3=WITHDRAWAL, 4=PAYMENT",
                allowableValues = {"1", "2", "3", "4"},
                example = "1"
        )
        @JsonProperty("operation_type_id")
        @NotNull(message = "Operation type must be informed")
        Long operationTypeId,

        @Schema(
                description = "Transaction amount. Always input as positive value - the sign is automatically adjusted based on operation type: types 1-3 become negative (debits), type 4 stays positive (credits)",
                example = "123.45"
        )
        @JsonProperty("amount")
        @NotNull(message = "Amount must be informed")
        @Positive
        BigDecimal amount) {
}
