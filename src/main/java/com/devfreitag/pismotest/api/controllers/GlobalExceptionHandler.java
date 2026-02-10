package com.devfreitag.pismotest.api.controllers;

import com.devfreitag.pismotest.exceptions.AccountConflictException;
import com.devfreitag.pismotest.exceptions.AccountNotFoundException;
import com.devfreitag.pismotest.exceptions.OperationTypeNotFoundException;
import com.devfreitag.pismotest.exceptions.TransactionInvalidException;
import com.devfreitag.pismotest.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(new ErrorResponse("Validation failed", errors));
    }

    @ExceptionHandler(AccountConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(final AccountConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage(), Collections.emptyMap()));
    }

    @ExceptionHandler(TransactionInvalidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidException(final TransactionInvalidException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErrorResponse(ex.getMessage(), Collections.emptyMap()));
    }



    @ExceptionHandler({AccountNotFoundException.class, OperationTypeNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleNotFound(final Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), Collections.emptyMap()));
    }
}
