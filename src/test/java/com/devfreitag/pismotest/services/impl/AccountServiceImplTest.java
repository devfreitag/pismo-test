package com.devfreitag.pismotest.services.impl;

import com.devfreitag.pismotest.entities.Account;
import com.devfreitag.pismotest.exceptions.AccountConflictException;
import com.devfreitag.pismotest.exceptions.AccountNotFoundException;
import com.devfreitag.pismotest.repositories.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountServiceImpl Tests")
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Nested
    @DisplayName("createAccount() Tests")
    class CreateAccountTests {

        @Nested
        @DisplayName("Success Scenarios")
        class SuccessScenarios {

            @Test
            @DisplayName("Should create account successfully with valid document number")
            void shouldCreateAccountSuccessfullyWithValidDocumentNumber() {
                // Arrange
                String documentNumber = "12345678900";
                when(accountRepository.existsByDocumentNumber(documentNumber))
                        .thenReturn(false);
                when(accountRepository.save(any(Account.class)))
                        .thenAnswer(invocation -> {
                            Account account = invocation.getArgument(0);
                            return Account.builder()
                                    .accountId(1L)
                                    .documentNumber(account.getDocumentNumber())
                                    .build();
                        });

                // Act
                Account result = accountService.createAccount(documentNumber);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getAccountId()).isEqualTo(1L);
                assertThat(result.getDocumentNumber()).isEqualTo(documentNumber);
                verify(accountRepository, times(1)).existsByDocumentNumber(documentNumber);
                verify(accountRepository, times(1)).save(any(Account.class));
                verifyNoMoreInteractions(accountRepository);
            }

            @Test
            @DisplayName("Should create account with different document number")
            void shouldCreateAccountWithDifferentDocumentNumber() {
                // Arrange
                String documentNumber = "98765432100";
                when(accountRepository.existsByDocumentNumber(documentNumber))
                        .thenReturn(false);
                when(accountRepository.save(any(Account.class)))
                        .thenAnswer(invocation -> {
                            Account account = invocation.getArgument(0);
                            return Account.builder()
                                    .accountId(2L)
                                    .documentNumber(account.getDocumentNumber())
                                    .build();
                        });

                // Act
                Account result = accountService.createAccount(documentNumber);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getAccountId()).isEqualTo(2L);
                assertThat(result.getDocumentNumber()).isEqualTo(documentNumber);
                verify(accountRepository, times(1)).existsByDocumentNumber(documentNumber);
                verify(accountRepository, times(1)).save(any(Account.class));
            }
        }

        @Nested
        @DisplayName("Conflict Scenarios")
        class ConflictScenarios {

            @Test
            @DisplayName("Should throw AccountConflictException when document number exists")
            void shouldThrowAccountConflictExceptionWhenDocumentNumberExists() {
                // Arrange
                String documentNumber = "12345678900";
                when(accountRepository.existsByDocumentNumber(documentNumber))
                        .thenReturn(true);

                // Act & Assert
                assertThatThrownBy(() -> accountService.createAccount(documentNumber))
                        .isInstanceOf(AccountConflictException.class);
                verify(accountRepository, times(1)).existsByDocumentNumber(documentNumber);
                verify(accountRepository, never()).save(any(Account.class));
            }

            @Test
            @DisplayName("Should verify exception message contains document number")
            void shouldVerifyExceptionMessageContainsDocumentNumber() {
                // Arrange
                String documentNumber = "12345678900";
                when(accountRepository.existsByDocumentNumber(documentNumber))
                        .thenReturn(true);

                // Act & Assert
                assertThatThrownBy(() -> accountService.createAccount(documentNumber))
                        .isInstanceOf(AccountConflictException.class)
                        .hasMessage("Account with document number " + documentNumber + " already exists.");
                verify(accountRepository, times(1)).existsByDocumentNumber(documentNumber);
                verify(accountRepository, never()).save(any(Account.class));
            }
        }

        @Nested
        @DisplayName("Edge Cases")
        class EdgeCases {

            @Test
            @DisplayName("Should handle null document number")
            void shouldHandleNullDocumentNumber() {
                // Arrange
                when(accountRepository.existsByDocumentNumber(null))
                        .thenReturn(false);
                when(accountRepository.save(any(Account.class)))
                        .thenAnswer(invocation -> {
                            Account account = invocation.getArgument(0);
                            return Account.builder()
                                    .accountId(1L)
                                    .documentNumber(account.getDocumentNumber())
                                    .build();
                        });

                // Act
                Account result = accountService.createAccount(null);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getDocumentNumber()).isNull();
                verify(accountRepository, times(1)).existsByDocumentNumber(null);
                verify(accountRepository, times(1)).save(any(Account.class));
            }

            @Test
            @DisplayName("Should handle empty string document number")
            void shouldHandleEmptyStringDocumentNumber() {
                // Arrange
                String documentNumber = "";
                when(accountRepository.existsByDocumentNumber(documentNumber))
                        .thenReturn(false);
                when(accountRepository.save(any(Account.class)))
                        .thenAnswer(invocation -> {
                            Account account = invocation.getArgument(0);
                            return Account.builder()
                                    .accountId(1L)
                                    .documentNumber(account.getDocumentNumber())
                                    .build();
                        });

                // Act
                Account result = accountService.createAccount(documentNumber);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getDocumentNumber()).isEqualTo("");
                verify(accountRepository, times(1)).existsByDocumentNumber(documentNumber);
                verify(accountRepository, times(1)).save(any(Account.class));
            }

            @Test
            @DisplayName("Should handle whitespace document number")
            void shouldHandleWhitespaceDocumentNumber() {
                // Arrange
                String documentNumber = "   ";
                when(accountRepository.existsByDocumentNumber(documentNumber))
                        .thenReturn(false);
                when(accountRepository.save(any(Account.class)))
                        .thenAnswer(invocation -> {
                            Account account = invocation.getArgument(0);
                            return Account.builder()
                                    .accountId(1L)
                                    .documentNumber(account.getDocumentNumber())
                                    .build();
                        });

                // Act
                Account result = accountService.createAccount(documentNumber);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getDocumentNumber()).isEqualTo("   ");
                verify(accountRepository, times(1)).existsByDocumentNumber(documentNumber);
                verify(accountRepository, times(1)).save(any(Account.class));
            }
        }
    }

    @Nested
    @DisplayName("findById() Tests")
    class FindByIdTests {

        @Nested
        @DisplayName("Success Scenarios")
        class SuccessScenarios {

            @Test
            @DisplayName("Should find account by ID successfully")
            void shouldFindAccountByIdSuccessfully() {
                // Arrange
                Long accountId = 1L;
                String documentNumber = "12345678900";
                Account mockAccount = Account.builder()
                        .accountId(accountId)
                        .documentNumber(documentNumber)
                        .build();
                when(accountRepository.findById(accountId))
                        .thenReturn(Optional.of(mockAccount));

                // Act
                Account result = accountService.findById(accountId);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getAccountId()).isEqualTo(accountId);
                assertThat(result.getDocumentNumber()).isEqualTo(documentNumber);
                verify(accountRepository, times(1)).findById(accountId);
                verifyNoMoreInteractions(accountRepository);
            }

            @Test
            @DisplayName("Should find account with different ID")
            void shouldFindAccountWithDifferentId() {
                // Arrange
                Long accountId = 99L;
                String documentNumber = "98765432100";
                Account mockAccount = Account.builder()
                        .accountId(accountId)
                        .documentNumber(documentNumber)
                        .build();
                when(accountRepository.findById(accountId))
                        .thenReturn(Optional.of(mockAccount));

                // Act
                Account result = accountService.findById(accountId);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getAccountId()).isEqualTo(accountId);
                assertThat(result.getDocumentNumber()).isEqualTo(documentNumber);
                verify(accountRepository, times(1)).findById(accountId);
            }
        }

        @Nested
        @DisplayName("Not Found Scenarios")
        class NotFoundScenarios {

            @Test
            @DisplayName("Should throw AccountNotFoundException when account does not exist")
            void shouldThrowAccountNotFoundExceptionWhenAccountDoesNotExist() {
                // Arrange
                Long accountId = 999L;
                when(accountRepository.findById(accountId))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> accountService.findById(accountId))
                        .isInstanceOf(AccountNotFoundException.class);
                verify(accountRepository, times(1)).findById(accountId);
                verifyNoMoreInteractions(accountRepository);
            }

            @Test
            @DisplayName("Should verify exception message contains account ID")
            void shouldVerifyExceptionMessageContainsAccountId() {
                // Arrange
                Long accountId = 999L;
                when(accountRepository.findById(accountId))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> accountService.findById(accountId))
                        .isInstanceOf(AccountNotFoundException.class)
                        .hasMessage("Account with ID " + accountId + " not found.");
                verify(accountRepository, times(1)).findById(accountId);
            }
        }

        @Nested
        @DisplayName("Edge Cases")
        class EdgeCases {

            @Test
            @DisplayName("Should handle null account ID")
            void shouldHandleNullAccountId() {
                // Arrange
                when(accountRepository.findById(null))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> accountService.findById(null))
                        .isInstanceOf(AccountNotFoundException.class);
                verify(accountRepository, times(1)).findById(null);
            }

            @Test
            @DisplayName("Should handle negative account ID")
            void shouldHandleNegativeAccountId() {
                // Arrange
                Long accountId = -1L;
                when(accountRepository.findById(accountId))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> accountService.findById(accountId))
                        .isInstanceOf(AccountNotFoundException.class)
                        .hasMessage("Account with ID " + accountId + " not found.");
                verify(accountRepository, times(1)).findById(accountId);
            }

            @Test
            @DisplayName("Should handle zero account ID")
            void shouldHandleZeroAccountId() {
                // Arrange
                Long accountId = 0L;
                when(accountRepository.findById(accountId))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> accountService.findById(accountId))
                        .isInstanceOf(AccountNotFoundException.class)
                        .hasMessage("Account with ID " + accountId + " not found.");
                verify(accountRepository, times(1)).findById(accountId);
            }
        }
    }
}
