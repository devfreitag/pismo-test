package com.devfreitag.pismotest.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CreateTransactionRequest(
        @JsonProperty("account_id")
        Long accountId,

        @JsonProperty("operation_type_id")
        Long operationTypeId,
        BigDecimal amount) {
}
