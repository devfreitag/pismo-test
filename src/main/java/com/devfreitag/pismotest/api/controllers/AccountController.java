package com.devfreitag.pismotest.api.controllers;

import com.devfreitag.pismotest.api.AccountAPI;
import com.devfreitag.pismotest.entities.Account;
import com.devfreitag.pismotest.models.CreateAccountRequest;
import com.devfreitag.pismotest.models.GetAccountResponse;
import com.devfreitag.pismotest.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountAPI {

    private final AccountService accountService;

    @Override
    public ResponseEntity<GetAccountResponse> createAccount(@RequestBody @Valid CreateAccountRequest request) {
        final Account account = this.accountService.createAccount(request.documentNumber());

        return ResponseEntity.ok(new GetAccountResponse(account.getAccountId(), account.getDocumentNumber()));
    }

    @Override
    public ResponseEntity<GetAccountResponse> getAccount(@PathVariable Long accountId) {
        final Account account = this.accountService.findById(accountId);

        return ResponseEntity.ok(new GetAccountResponse(account.getAccountId(), account.getDocumentNumber()));
    }
}
