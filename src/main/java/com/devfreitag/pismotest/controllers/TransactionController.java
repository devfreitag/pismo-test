package com.devfreitag.pismotest.controllers;

import com.devfreitag.pismotest.entities.Transaction;
import com.devfreitag.pismotest.models.CreateTransactionRequest;
import com.devfreitag.pismotest.models.CreateTransactionResponse;
import com.devfreitag.pismotest.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Object> createTransaction(@RequestBody CreateTransactionRequest request) {
        final Transaction aTransaction = transactionService.createTransaction(request.accountId(), request.operationTypeId(), request.amount());

        return ResponseEntity.ok(new CreateTransactionResponse(
                aTransaction.getTransactionId(),
                aTransaction.getAccount().getAccountId(),
                aTransaction.getOperationType().getOperationTypeId(),
                aTransaction.getAmount()
        ));

    }
}
