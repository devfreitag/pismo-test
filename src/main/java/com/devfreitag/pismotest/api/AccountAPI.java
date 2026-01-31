package com.devfreitag.pismotest.api;

import com.devfreitag.pismotest.models.CreateAccountRequest;
import com.devfreitag.pismotest.models.GetAccountResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "accounts")
public interface AccountAPI {

    @PostMapping
    ResponseEntity<GetAccountResponse> createAccount(@RequestBody CreateAccountRequest request);

    @GetMapping("/{accountId}")
    ResponseEntity<GetAccountResponse> getAccount(@PathVariable Long accountId);
}
