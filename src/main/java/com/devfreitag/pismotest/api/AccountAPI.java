package com.devfreitag.pismotest.api;

import com.devfreitag.pismotest.models.CreateAccountRequest;
import com.devfreitag.pismotest.models.ErrorResponse;
import com.devfreitag.pismotest.models.GetAccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Accounts", description = "Account management operations")
@RequestMapping(value = "accounts")
public interface AccountAPI {

    @Operation(
            summary = "Create a new account",
            description = "Creates a new account with the provided document number"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account created successfully",
                    content = @Content(schema = @Schema(implementation = GetAccountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error - document number is missing or invalid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    ResponseEntity<GetAccountResponse> createAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Account creation request",
                    required = true
            )
            @RequestBody CreateAccountRequest request
    );

    @Operation(
            summary = "Get account by ID",
            description = "Retrieves account details by the account ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account found",
                    content = @Content(schema = @Schema(implementation = GetAccountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{accountId}")
    ResponseEntity<GetAccountResponse> getAccount(
            @Parameter(description = "ID of the account to retrieve", required = true, example = "1")
            @PathVariable Long accountId
    );
}
