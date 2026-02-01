package com.devfreitag.pismotest.api.controllers;

import com.devfreitag.pismotest.exceptions.AccountConflictException;
import com.devfreitag.pismotest.exceptions.AccountNotFoundException;
import com.devfreitag.pismotest.exceptions.OperationTypeNotFoundException;
import com.devfreitag.pismotest.models.CreateAccountRequest;
import com.devfreitag.pismotest.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AccountController.class, GlobalExceptionHandler.class})
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AccountService accountService;

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException and return 400 with field errors")
    void shouldHandleMethodArgumentNotValidException() throws Exception {
        // Arrange
        String requestBody = "{\"document_number\": null}";

        // Act & Assert
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors", hasKey("documentNumber")))
                .andExpect(jsonPath("$.errors.documentNumber").value("Document number is required"));
    }

    @Test
    @DisplayName("Should handle AccountConflictException and return 409")
    void shouldHandleAccountConflictException() throws Exception {
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

    @Test
    @DisplayName("Should handle AccountNotFoundException and return 404")
    void shouldHandleAccountNotFoundException() throws Exception {
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
    @DisplayName("Should handle OperationTypeNotFoundException and return 404")
    void shouldHandleOperationTypeNotFoundException() throws Exception {
        // Arrange
        Long accountId = 1L;

        when(accountService.findById(accountId))
                .thenThrow(new OperationTypeNotFoundException(999L));

        // Act & Assert
        mockMvc.perform(get("/accounts/{accountId}", accountId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Operation type with ID 999 not found."))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("Should include multiple validation errors when multiple fields are invalid")
    void shouldIncludeMultipleValidationErrors() throws Exception {
        // Arrange
        String requestBody = "{}";

        // Act & Assert
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors", hasKey("documentNumber")))
                .andExpect(jsonPath("$.errors.documentNumber").value("Document number is required"));
    }
}
