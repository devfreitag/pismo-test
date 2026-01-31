package com.devfreitag.pismotest.api;

import com.devfreitag.pismotest.models.CreateTransactionRequest;
import com.devfreitag.pismotest.models.CreateTransactionResponse;
import com.devfreitag.pismotest.models.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Transactions", description = "Transaction management operations")
@RequestMapping(value = "transactions")
public interface TransactionAPI {

    @Operation(
            summary = "Create a new transaction",
            description = """
                    Creates a new transaction for the specified account.

                    ## Amount Sign Adjustment
                    The transaction amount is automatically adjusted based on the operation type:
                    - **Types 1, 2, 3** (PURCHASE, INSTALLMENT_PURCHASE, WITHDRAWAL): Amount becomes **negative** (debit)
                    - **Type 4** (PAYMENT): Amount remains **positive** (credit)

                    Always input the amount as a positive value; the system handles the sign conversion.

                    ## Operation Types
                    | ID | Name | Amount Sign |
                    |----|------|-------------|
                    | 1 | PURCHASE | Negative |
                    | 2 | INSTALLMENT_PURCHASE | Negative |
                    | 3 | WITHDRAWAL | Negative |
                    | 4 | PAYMENT | Positive |
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transaction created successfully",
                    content = @Content(
                            schema = @Schema(implementation = CreateTransactionResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Purchase Response",
                                            summary = "Purchase transaction (amount becomes negative)",
                                            value = """
                                                    {
                                                      "transaction_id": 1,
                                                      "account_id": 1,
                                                      "operation_type_id": 1,
                                                      "amount": -123.45
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Payment Response",
                                            summary = "Payment transaction (amount stays positive)",
                                            value = """
                                                    {
                                                      "transaction_id": 2,
                                                      "account_id": 1,
                                                      "operation_type_id": 4,
                                                      "amount": 500.00
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error - required fields missing or invalid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account or operation type not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    ResponseEntity<CreateTransactionResponse> createTransaction(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transaction creation request",
                    required = true,
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "Purchase",
                                            summary = "Purchase transaction (type 1)",
                                            description = "Amount 123.45 will become -123.45 in the response",
                                            value = """
                                                    {
                                                      "account_id": 1,
                                                      "operation_type_id": 1,
                                                      "amount": 123.45
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Payment",
                                            summary = "Payment transaction (type 4)",
                                            description = "Amount 500.00 will stay 500.00 in the response",
                                            value = """
                                                    {
                                                      "account_id": 1,
                                                      "operation_type_id": 4,
                                                      "amount": 500.00
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @RequestBody @Valid CreateTransactionRequest request
    );
}
