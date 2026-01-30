package com.devfreitag.pismotest.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(final Long accountId) {
        super("Account with ID " + accountId + " not found.");
    }
}
