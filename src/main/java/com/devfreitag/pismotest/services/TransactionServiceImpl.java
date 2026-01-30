package com.devfreitag.pismotest.services;

import com.devfreitag.pismotest.entities.Account;
import com.devfreitag.pismotest.entities.OperationType;
import com.devfreitag.pismotest.entities.Transaction;
import com.devfreitag.pismotest.enums.OperationTypeEnum;
import com.devfreitag.pismotest.exceptions.AccountNotFoundException;
import com.devfreitag.pismotest.exceptions.OperationTypeNotFoundException;
import com.devfreitag.pismotest.repositories.AccountRepository;
import com.devfreitag.pismotest.repositories.OperationTypeRepository;
import com.devfreitag.pismotest.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final OperationTypeRepository operationTypeRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Long accountId, Long operationTypeId, BigDecimal amount) {
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        final OperationType operationType = operationTypeRepository.findById(operationTypeId)
                .orElseThrow(() -> new OperationTypeNotFoundException(operationTypeId));

        OperationTypeEnum operationTypeEnum = OperationTypeEnum.fromCode(operationTypeId);

        switch (operationTypeEnum) {
            case PAYMENT:
                amount = amount.abs();
                break;
            case INSTALLMENT_PURCHASE,PURCHASE,WITHDRAWAL:
                amount = amount.abs().negate();
        }

        Transaction transaction = Transaction.builder()
                .account(account)
                .operationType(operationType)
                .amount(amount)
                .eventDate(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }
}
