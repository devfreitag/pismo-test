package com.devfreitag.pismotest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Response containing the created transaction details")
public record CreateTransactionResponse(
        @Schema(description = "Unique identifier for the created transaction", example = "1")
        @JsonProperty("transaction_id")
        Long transactionId,

        @Schema(description = "ID of the account associated with this transaction", example = "1")
        @JsonProperty("account_id")
        Long accountId,

        @Schema(description = "Operation type ID", example = "1")
        @JsonProperty("operation_type_id")
        Long operationTypeId,

        @Schema(description = "Final transaction amount after sign adjustment. Negative for debits (types 1-3), positive for credits (type 4)", example = "-123.45")
        BigDecimal amount
) {}
