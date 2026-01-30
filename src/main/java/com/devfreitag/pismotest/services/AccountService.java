package com.devfreitag.pismotest.services;

import com.devfreitag.pismotest.entities.Account;

public interface AccountService {
    Account createAccount(String documentNumber);
    Account findById(Long accountId);
}
