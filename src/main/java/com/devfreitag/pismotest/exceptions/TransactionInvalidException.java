package com.devfreitag.pismotest.exceptions;

import java.math.BigDecimal;

public class TransactionInvalidException extends RuntimeException {
    public TransactionInvalidException() {
        super("Transaction with credit limit can't be less than zero.");
    }
}
