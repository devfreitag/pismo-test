package com.devfreitag.pismotest.api.controllers;

import com.devfreitag.pismotest.api.TransactionAPI;
import com.devfreitag.pismotest.entities.Transaction;
import com.devfreitag.pismotest.models.CreateTransactionRequest;
import com.devfreitag.pismotest.models.CreateTransactionResponse;
import com.devfreitag.pismotest.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionAPI {

    private final TransactionService transactionService;

    @Override
    public ResponseEntity<CreateTransactionResponse> createTransaction(@RequestBody @Valid CreateTransactionRequest request) {
        final Transaction transaction = transactionService.createTransaction(request.accountId(), request.operationTypeId(), request.amount());

        return ResponseEntity.ok(new CreateTransactionResponse(
                transaction.getTransactionId(),
                transaction.getAccount().getAccountId(),
                transaction.getOperationType().getOperationTypeId(),
                transaction.getAmount()
        ));

    }
}
