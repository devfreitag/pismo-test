package com.devfreitag.pismotest.exceptions;

public class OperationTypeNotFoundException extends RuntimeException {
    public OperationTypeNotFoundException(final Long operationTypeId) {
        super("Operation type with ID " + operationTypeId + " not found.");
    }
}
