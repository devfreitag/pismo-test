package com.devfreitag.pismotest.services;

import com.devfreitag.pismotest.entities.Transaction;

import java.math.BigDecimal;

public interface TransactionService {
    Transaction createTransaction(Long accountId, Long operationType, BigDecimal amount);
}
