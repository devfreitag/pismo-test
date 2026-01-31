package com.devfreitag.pismotest.api;

import com.devfreitag.pismotest.models.CreateTransactionRequest;
import com.devfreitag.pismotest.models.CreateTransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "transactions")
public interface TransactionAPI {

    @PostMapping
    ResponseEntity<CreateTransactionResponse> createTransaction(@RequestBody @Valid CreateTransactionRequest request);
}
