package com.devfreitag.pismotest.controllers;

import com.devfreitag.pismotest.entities.Account;
import com.devfreitag.pismotest.models.CreateAccountRequest;
import com.devfreitag.pismotest.models.GetAccountResponse;
import com.devfreitag.pismotest.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<GetAccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        final Account anAccount = this.accountService.createAccount(request.documentNumber());

        return ResponseEntity.ok(new GetAccountResponse(anAccount.getAccountId(), anAccount.getDocumentNumber()));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<GetAccountResponse> getAccount(@PathVariable Long accountId) {
        final Account anAccount = this.accountService.findById(accountId);

        return ResponseEntity.ok(new GetAccountResponse(anAccount.getAccountId(), anAccount.getDocumentNumber()));
    }
}
