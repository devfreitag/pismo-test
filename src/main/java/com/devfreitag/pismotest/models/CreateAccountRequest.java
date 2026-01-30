package com.devfreitag.pismotest.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAccountRequest(@JsonProperty("document_number") String documentNumber) {
}
