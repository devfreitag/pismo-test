package com.devfreitag.pismotest.api.controllers;

import com.devfreitag.pismotest.entities.Account;
import com.devfreitag.pismotest.entities.OperationType;
import com.devfreitag.pismotest.entities.Transaction;
import com.devfreitag.pismotest.exceptions.AccountNotFoundException;
import com.devfreitag.pismotest.exceptions.OperationTypeNotFoundException;
import com.devfreitag.pismotest.models.CreateTransactionRequest;
import com.devfreitag.pismotest.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@DisplayName("TransactionController Tests")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private TransactionService transactionService;

    @Nested
    @DisplayName("POST /transactions - Create Transaction")
    class CreateTransactionTests {

        @Nested
        @DisplayName("Success Scenarios")
        class SuccessScenarios {

            @Test
            @DisplayName("Should create purchase transaction successfully (operation type 1, amount becomes negative)")
            void shouldCreatePurchaseTransactionSuccessfully() throws Exception {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal amount = new BigDecimal("123.45");
                BigDecimal expectedAmount = new BigDecimal("-123.45");

                CreateTransactionRequest request = new CreateTransactionRequest(accountId, operationTypeId, amount);

                Transaction mockTransaction = createMockTransaction(1L, accountId, operationTypeId, expectedAmount);

                when(transactionService.createTransaction(accountId, operationTypeId, amount))
                        .thenReturn(mockTransaction);

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", "/transactions/1"))
                        .andExpect(jsonPath("$.transaction_id").value(1))
                        .andExpect(jsonPath("$.account_id").value(accountId))
                        .andExpect(jsonPath("$.operation_type_id").value(operationTypeId))
                        .andExpect(jsonPath("$.amount").value(expectedAmount.doubleValue()));
            }

            @Test
            @DisplayName("Should create installment purchase transaction successfully (operation type 2, amount becomes negative)")
            void shouldCreateInstallmentPurchaseTransactionSuccessfully() throws Exception {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 2L;
                BigDecimal amount = new BigDecimal("250.00");
                BigDecimal expectedAmount = new BigDecimal("-250.00");

                CreateTransactionRequest request = new CreateTransactionRequest(accountId, operationTypeId, amount);

                Transaction mockTransaction = createMockTransaction(2L, accountId, operationTypeId, expectedAmount);

                when(transactionService.createTransaction(accountId, operationTypeId, amount))
                        .thenReturn(mockTransaction);

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", "/transactions/2"))
                        .andExpect(jsonPath("$.transaction_id").value(2))
                        .andExpect(jsonPath("$.account_id").value(accountId))
                        .andExpect(jsonPath("$.operation_type_id").value(operationTypeId))
                        .andExpect(jsonPath("$.amount").value(expectedAmount.doubleValue()));
            }

            @Test
            @DisplayName("Should create withdrawal transaction successfully (operation type 3, amount becomes negative)")
            void shouldCreateWithdrawalTransactionSuccessfully() throws Exception {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 3L;
                BigDecimal amount = new BigDecimal("50.00");
                BigDecimal expectedAmount = new BigDecimal("-50.00");

                CreateTransactionRequest request = new CreateTransactionRequest(accountId, operationTypeId, amount);

                Transaction mockTransaction = createMockTransaction(3L, accountId, operationTypeId, expectedAmount);

                when(transactionService.createTransaction(accountId, operationTypeId, amount))
                        .thenReturn(mockTransaction);

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", "/transactions/3"))
                        .andExpect(jsonPath("$.transaction_id").value(3))
                        .andExpect(jsonPath("$.account_id").value(accountId))
                        .andExpect(jsonPath("$.operation_type_id").value(operationTypeId))
                        .andExpect(jsonPath("$.amount").value(expectedAmount.doubleValue()));
            }

            @Test
            @DisplayName("Should create payment transaction successfully (operation type 4, amount stays positive)")
            void shouldCreatePaymentTransactionSuccessfully() throws Exception {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 4L;
                BigDecimal amount = new BigDecimal("100.00");
                BigDecimal expectedAmount = new BigDecimal("100.00");

                CreateTransactionRequest request = new CreateTransactionRequest(accountId, operationTypeId, amount);

                Transaction mockTransaction = createMockTransaction(4L, accountId, operationTypeId, expectedAmount);

                when(transactionService.createTransaction(accountId, operationTypeId, amount))
                        .thenReturn(mockTransaction);

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", "/transactions/4"))
                        .andExpect(jsonPath("$.transaction_id").value(4))
                        .andExpect(jsonPath("$.account_id").value(accountId))
                        .andExpect(jsonPath("$.operation_type_id").value(operationTypeId))
                        .andExpect(jsonPath("$.amount").value(expectedAmount.doubleValue()));
            }
        }

        @Nested
        @DisplayName("Validation Errors")
        class ValidationErrors {

            @Test
            @DisplayName("Should return 400 when account ID is null")
            void shouldReturn400WhenAccountIdIsNull() throws Exception {
                // Arrange
                String requestBody = "{\"account_id\": null, \"operation_type_id\": 1, \"amount\": 100.00}";

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").exists())
                        .andExpect(jsonPath("$.errors.accountId").value("Account must be informed"));
            }

            @Test
            @DisplayName("Should return 400 when operation type ID is null")
            void shouldReturn400WhenOperationTypeIdIsNull() throws Exception {
                // Arrange
                String requestBody = "{\"account_id\": 1, \"operation_type_id\": null, \"amount\": 100.00}";

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").exists())
                        .andExpect(jsonPath("$.errors.operationTypeId").value("Operation type must be informed"));
            }

            @Test
            @DisplayName("Should return 400 when amount is null")
            void shouldReturn400WhenAmountIsNull() throws Exception {
                // Arrange
                String requestBody = "{\"account_id\": 1, \"operation_type_id\": 1, \"amount\": null}";

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").exists())
                        .andExpect(jsonPath("$.errors.amount").value("Amount must be informed"));
            }

            @Test
            @DisplayName("Should return 400 when amount is negative")
            void shouldReturn400WhenAmountIsNegative() throws Exception {
                // Arrange
                String requestBody = "{\"account_id\": 1, \"operation_type_id\": 1, \"amount\": -100.00}";

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").exists())
                        .andExpect(jsonPath("$.errors.amount").exists());
            }

            @Test
            @DisplayName("Should return 400 when amount is zero")
            void shouldReturn400WhenAmountIsZero() throws Exception {
                // Arrange
                String requestBody = "{\"account_id\": 1, \"operation_type_id\": 1, \"amount\": 0}";

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").exists())
                        .andExpect(jsonPath("$.errors.amount").exists());
            }

            @Test
            @DisplayName("Should return 400 when all fields are missing")
            void shouldReturn400WhenAllFieldsAreMissing() throws Exception {
                // Arrange
                String requestBody = "{}";

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").exists())
                        .andExpect(jsonPath("$.errors.accountId").value("Account must be informed"))
                        .andExpect(jsonPath("$.errors.operationTypeId").value("Operation type must be informed"))
                        .andExpect(jsonPath("$.errors.amount").value("Amount must be informed"));
            }
        }

        @Nested
        @DisplayName("Business Errors")
        class BusinessErrors {

            @Test
            @DisplayName("Should return 404 when account is not found")
            void shouldReturn404WhenAccountNotFound() throws Exception {
                // Arrange
                Long accountId = 999L;
                Long operationTypeId = 1L;
                BigDecimal amount = new BigDecimal("100.00");

                CreateTransactionRequest request = new CreateTransactionRequest(accountId, operationTypeId, amount);

                when(transactionService.createTransaction(accountId, operationTypeId, amount))
                        .thenThrow(new AccountNotFoundException(accountId));

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message").value("Account with ID " + accountId + " not found."))
                        .andExpect(jsonPath("$.errors").isEmpty());
            }

            @Test
            @DisplayName("Should return 404 when operation type is not found")
            void shouldReturn404WhenOperationTypeNotFound() throws Exception {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 999L;
                BigDecimal amount = new BigDecimal("100.00");

                CreateTransactionRequest request = new CreateTransactionRequest(accountId, operationTypeId, amount);

                when(transactionService.createTransaction(accountId, operationTypeId, amount))
                        .thenThrow(new OperationTypeNotFoundException(operationTypeId));

                // Act & Assert
                mockMvc.perform(post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message").value("Operation type with ID " + operationTypeId + " not found."))
                        .andExpect(jsonPath("$.errors").isEmpty());
            }
        }
    }

    private Transaction createMockTransaction(Long transactionId, Long accountId, Long operationTypeId, BigDecimal amount) {
        Transaction mockTransaction = mock(Transaction.class);
        Account mockAccount = mock(Account.class);
        OperationType mockOperationType = mock(OperationType.class);

        when(mockTransaction.getTransactionId()).thenReturn(transactionId);
        when(mockTransaction.getAccount()).thenReturn(mockAccount);
        when(mockAccount.getAccountId()).thenReturn(accountId);
        when(mockTransaction.getOperationType()).thenReturn(mockOperationType);
        when(mockOperationType.getOperationTypeId()).thenReturn(operationTypeId);
        when(mockTransaction.getAmount()).thenReturn(amount);

        return mockTransaction;
    }
}
