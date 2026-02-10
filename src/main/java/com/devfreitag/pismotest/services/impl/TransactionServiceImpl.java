package com.devfreitag.pismotest.services.impl;

import com.devfreitag.pismotest.entities.Transaction;
import com.devfreitag.pismotest.enums.OperationTypeEnum;
import com.devfreitag.pismotest.exceptions.AccountNotFoundException;
import com.devfreitag.pismotest.exceptions.OperationTypeNotFoundException;
import com.devfreitag.pismotest.exceptions.TransactionInvalidException;
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

        BigDecimal creditLimit = BigDecimal.ZERO;
        if (operationTypeEnum == OperationTypeEnum.PAYMENT) {
            amount = amount.abs();

            creditLimit = account.getAvailableCreditLimit().add(amount);
            account.setAvailableCreditLimit(account.getAvailableCreditLimit().add(amount));
        } else if (operationTypeEnum == OperationTypeEnum.INSTALLMENT_PURCHASE || operationTypeEnum == OperationTypeEnum.PURCHASE || operationTypeEnum == OperationTypeEnum.WITHDRAWAL) {
            amount = amount.abs().negate();

            creditLimit = account.getAvailableCreditLimit().add(amount);
        }

        if (creditLimit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionInvalidException();
        }

        account.setAvailableCreditLimit(creditLimit);
        accountRepository.save(account);

        var transaction = Transaction.builder()
                .account(account)
                .operationType(operationType)
                .amount(amount)
                .eventDate(Instant.now())
                .build();

        return transactionRepository.save(transaction);
    }
}
