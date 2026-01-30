package com.devfreitag.pismotest.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetAccountResponse(
        @JsonProperty("account_id")
        Long accountId,
        @JsonProperty("document_number")
        String documentNumber) {
}
