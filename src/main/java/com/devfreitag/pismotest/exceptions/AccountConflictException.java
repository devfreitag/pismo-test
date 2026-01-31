package com.devfreitag.pismotest.exceptions;

public class AccountConflictException extends RuntimeException {
    public AccountConflictException(final String documentNumber) {
        super("Account with document number " + documentNumber + " already exists.");
    }
}
