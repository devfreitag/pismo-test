package com.devfreitag.pismotest.api.controllers;

import com.devfreitag.pismotest.entities.Account;
import com.devfreitag.pismotest.exceptions.AccountConflictException;
import com.devfreitag.pismotest.exceptions.AccountNotFoundException;
import com.devfreitag.pismotest.models.CreateAccountRequest;
import com.devfreitag.pismotest.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@DisplayName("AccountController Tests")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AccountService accountService;

    @Nested
    @DisplayName("POST /accounts - Create Account")
    class CreateAccountTests {

        @Nested
        @DisplayName("Success Scenarios")
        class SuccessScenarios {

            @Test
            @DisplayName("Should create account successfully with valid document number")
            void shouldCreateAccountSuccessfully() throws Exception {
                // Arrange
                String documentNumber = "12345678900";
                CreateAccountRequest request = new CreateAccountRequest(documentNumber);

                Account mockAccount = Account.builder()
                        .accountId(1L)
                        .documentNumber(documentNumber)
                        .build();

                when(accountService.createAccount(documentNumber))
                        .thenReturn(mockAccount);

                // Act & Assert
                mockMvc.perform(post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", "/accounts/1"))
                        .andExpect(jsonPath("$.account_id").value(1))
                        .andExpect(jsonPath("$.document_number").value(documentNumber));
            }
        }

        @Nested
        @DisplayName("Validation Errors")
        class ValidationErrors {

            @Test
            @DisplayName("Should return 400 when document number is blank")
            void shouldReturn400WhenDocumentNumberIsBlank() throws Exception {
                // Arrange
                String requestBody = "{\"document_number\": \"  \"}";

                // Act & Assert
                mockMvc.perform(post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").exists())
                        .andExpect(jsonPath("$.errors.documentNumber").value("Document number is required"));
            }

            @Test
            @DisplayName("Should return 400 when document number is null")
            void shouldReturn400WhenDocumentNumberIsNull() throws Exception {
                // Arrange
                String requestBody = "{\"document_number\": null}";

                // Act & Assert
                mockMvc.perform(post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").exists())
                        .andExpect(jsonPath("$.errors.documentNumber").value("Document number is required"));
            }

            @Test
            @DisplayName("Should return 400 when document number is missing")
            void shouldReturn400WhenDocumentNumberIsMissing() throws Exception {
                // Arrange
                String requestBody = "{}";

                // Act & Assert
                mockMvc.perform(post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").exists())
                        .andExpect(jsonPath("$.errors.documentNumber").value("Document number is required"));
            }
        }

        @Nested
        @DisplayName("Business Errors")
        class BusinessErrors {

            @Test
            @DisplayName("Should return 409 when account with document number already exists")
            void shouldReturn409WhenAccountConflictExceptionThrown() throws Exception {
                // Arrange
                String documentNumber = "12345678900";
                CreateAccountRequest request = new CreateAccountRequest(documentNumber);

                when(accountService.createAccount(documentNumber))
                        .thenThrow(new AccountConflictException(documentNumber));

                // Act & Assert
                mockMvc.perform(post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isConflict())
                        .andExpect(jsonPath("$.message").value("Account with document number " + documentNumber + " already exists."))
                        .andExpect(jsonPath("$.errors").isEmpty());
            }
        }
    }

    @Nested
    @DisplayName("GET /accounts/{accountId} - Get Account")
    class GetAccountTests {

        @Nested
        @DisplayName("Success Scenarios")
        class SuccessScenarios {

            @Test
            @DisplayName("Should get account successfully with valid account ID")
            void shouldGetAccountSuccessfully() throws Exception {
                // Arrange
                Long accountId = 1L;
                String documentNumber = "12345678900";

                Account mockAccount = Account.builder()
                        .accountId(accountId)
                        .documentNumber(documentNumber)
                        .build();

                when(accountService.findById(accountId))
                        .thenReturn(mockAccount);

                // Act & Assert
                mockMvc.perform(get("/accounts/{accountId}", accountId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.account_id").value(accountId))
                        .andExpect(jsonPath("$.document_number").value(documentNumber));
            }
        }

        @Nested
        @DisplayName("Error Scenarios")
        class ErrorScenarios {

            @Test
            @DisplayName("Should return 404 when account is not found")
            void shouldReturn404WhenAccountNotFound() throws Exception {
                // Arrange
                Long accountId = 999L;

                when(accountService.findById(accountId))
                        .thenThrow(new AccountNotFoundException(accountId));

                // Act & Assert
                mockMvc.perform(get("/accounts/{accountId}", accountId))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message").value("Account with ID " + accountId + " not found."))
                        .andExpect(jsonPath("$.errors").isEmpty());
            }

            @Test
            @DisplayName("Should handle invalid account ID format")
            void shouldHandleInvalidAccountIdFormat() throws Exception {
                // Act & Assert
                mockMvc.perform(get("/accounts/{accountId}", "invalid"))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
