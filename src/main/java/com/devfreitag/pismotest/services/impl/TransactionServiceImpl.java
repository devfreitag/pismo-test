package com.devfreitag.pismotest.services.impl;

import com.devfreitag.pismotest.entities.Transaction;
import com.devfreitag.pismotest.enums.OperationTypeEnum;
import com.devfreitag.pismotest.exceptions.AccountNotFoundException;
import com.devfreitag.pismotest.exceptions.OperationTypeNotFoundException;
import com.devfreitag.pismotest.repositories.AccountRepository;
import com.devfreitag.pismotest.repositories.OperationTypeRepository;
import com.devfreitag.pismotest.repositories.TransactionRepository;
import com.devfreitag.pismotest.services.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final OperationTypeRepository operationTypeRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public Transaction createTransaction(Long accountId, Long operationTypeId, BigDecimal amount) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        var operationType = operationTypeRepository.findById(operationTypeId)
                .orElseThrow(() -> new OperationTypeNotFoundException(operationTypeId));

        var operationTypeEnum = OperationTypeEnum.fromCode(operationTypeId);

        switch (operationTypeEnum) {
            case PAYMENT:
                amount = amount.abs();
                break;
            case INSTALLMENT_PURCHASE,PURCHASE,WITHDRAWAL:
                amount = amount.abs().negate();
        }

        var transaction = Transaction.builder()
                .account(account)
                .operationType(operationType)
                .amount(amount)
                .eventDate(Instant.now())
                .build();

        return transactionRepository.save(transaction);
    }
}
