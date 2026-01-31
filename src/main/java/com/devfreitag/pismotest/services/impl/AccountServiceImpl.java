package com.devfreitag.pismotest.services.impl;

import com.devfreitag.pismotest.entities.Account;
import com.devfreitag.pismotest.exceptions.AccountNotFoundException;
import com.devfreitag.pismotest.repositories.AccountRepository;
import com.devfreitag.pismotest.services.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Account createAccount(String documentNumber) {
        final Account account = Account.builder().documentNumber(documentNumber).build();

        return this.accountRepository.save(account);
    }

    @Override
    public Account findById(Long accountId) {
        return this.accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
