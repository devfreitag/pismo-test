package com.devfreitag.pismotest.services.impl;

import com.devfreitag.pismotest.entities.Account;
import com.devfreitag.pismotest.entities.OperationType;
import com.devfreitag.pismotest.entities.Transaction;
import com.devfreitag.pismotest.exceptions.AccountNotFoundException;
import com.devfreitag.pismotest.exceptions.OperationTypeNotFoundException;
import com.devfreitag.pismotest.repositories.AccountRepository;
import com.devfreitag.pismotest.repositories.OperationTypeRepository;
import com.devfreitag.pismotest.repositories.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionServiceImpl Tests")
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationTypeRepository operationTypeRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    // Helper methods
    private Account buildAccount(Long accountId, String documentNumber, BigDecimal availableCreditLimit) {
        return Account.builder()
                .accountId(accountId)
                .documentNumber(documentNumber)
                .availableCreditLimit(availableCreditLimit)
                .build();
    }

    private OperationType buildOperationType(Long operationTypeId, String description) {
        OperationType operationType = new OperationType();
        ReflectionTestUtils.setField(operationType, "operationTypeId", operationTypeId);
        ReflectionTestUtils.setField(operationType, "description", description);
        return operationType;
    }

    private void mockAccountRepository(Long accountId, String documentNumber) {
        mockAccountRepository(accountId, documentNumber, new BigDecimal("9999999999.99"));
    }

    private void mockAccountRepository(Long accountId, String documentNumber, BigDecimal availableCreditLimit) {
        Account mockAccount = buildAccount(accountId, documentNumber, availableCreditLimit);
        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(mockAccount));
    }

    private void mockOperationTypeRepository(Long operationTypeId, String description) {
        OperationType mockOperationType = buildOperationType(operationTypeId, description);
        when(operationTypeRepository.findById(operationTypeId))
                .thenReturn(Optional.of(mockOperationType));
    }

    private void mockTransactionRepositorySave() {
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> {
                    Transaction transaction = invocation.getArgument(0);
                    return Transaction.builder()
                            .transactionId(1L)
                            .account(transaction.getAccount())
                            .operationType(transaction.getOperationType())
                            .amount(transaction.getAmount())
                            .eventDate(transaction.getEventDate())
                            .build();
                });
    }

    @Nested
    @DisplayName("createTransaction() Tests")
    class CreateTransactionTests {

        @Nested
        @DisplayName("Purchase Transaction Tests - Type 1")
        class PurchaseTransactionTests {

            @Test
            @DisplayName("Should create purchase transaction with positive amount")
            void shouldCreatePurchaseTransactionWithPositiveAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");
                BigDecimal expectedAmount = new BigDecimal("-100.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PURCHASE");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }

            @Test
            @DisplayName("Should create purchase transaction with negative amount")
            void shouldCreatePurchaseTransactionWithNegativeAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("-100.00");
                BigDecimal expectedAmount = new BigDecimal("-100.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PURCHASE");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }

            @Test
            @DisplayName("Should create purchase transaction with zero amount")
            void shouldCreatePurchaseTransactionWithZeroAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("0.00");
                BigDecimal expectedAmount = new BigDecimal("0.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PURCHASE");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }

            @Test
            @DisplayName("Should create purchase transaction with decimal amount")
            void shouldCreatePurchaseTransactionWithDecimalAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("123.45");
                BigDecimal expectedAmount = new BigDecimal("-123.45");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PURCHASE");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }
        }

        @Nested
        @DisplayName("Installment Purchase Transaction Tests - Type 2")
        class InstallmentPurchaseTransactionTests {

            @Test
            @DisplayName("Should create installment purchase transaction with positive amount")
            void shouldCreateInstallmentPurchaseTransactionWithPositiveAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 2L;
                BigDecimal inputAmount = new BigDecimal("250.00");
                BigDecimal expectedAmount = new BigDecimal("-250.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "INSTALLMENT_PURCHASE");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }

            @Test
            @DisplayName("Should create installment purchase transaction with negative amount")
            void shouldCreateInstallmentPurchaseTransactionWithNegativeAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 2L;
                BigDecimal inputAmount = new BigDecimal("-250.00");
                BigDecimal expectedAmount = new BigDecimal("-250.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "INSTALLMENT_PURCHASE");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }

            @Test
            @DisplayName("Should create installment purchase transaction with decimal amount")
            void shouldCreateInstallmentPurchaseTransactionWithDecimalAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 2L;
                BigDecimal inputAmount = new BigDecimal("99.99");
                BigDecimal expectedAmount = new BigDecimal("-99.99");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "INSTALLMENT_PURCHASE");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }
        }

        @Nested
        @DisplayName("Withdrawal Transaction Tests - Type 3")
        class WithdrawalTransactionTests {

            @Test
            @DisplayName("Should create withdrawal transaction with positive amount")
            void shouldCreateWithdrawalTransactionWithPositiveAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 3L;
                BigDecimal inputAmount = new BigDecimal("50.00");
                BigDecimal expectedAmount = new BigDecimal("-50.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "WITHDRAWAL");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }

            @Test
            @DisplayName("Should create withdrawal transaction with negative amount")
            void shouldCreateWithdrawalTransactionWithNegativeAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 3L;
                BigDecimal inputAmount = new BigDecimal("-50.00");
                BigDecimal expectedAmount = new BigDecimal("-50.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "WITHDRAWAL");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }

            @Test
            @DisplayName("Should create withdrawal transaction with decimal amount")
            void shouldCreateWithdrawalTransactionWithDecimalAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 3L;
                BigDecimal inputAmount = new BigDecimal("75.50");
                BigDecimal expectedAmount = new BigDecimal("-75.50");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "WITHDRAWAL");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }
        }

        @Nested
        @DisplayName("Payment Transaction Tests - Type 4")
        class PaymentTransactionTests {

            @Test
            @DisplayName("Should create payment transaction with positive amount")
            void shouldCreatePaymentTransactionWithPositiveAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 4L;
                BigDecimal inputAmount = new BigDecimal("100.00");
                BigDecimal expectedAmount = new BigDecimal("100.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PAYMENT");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }

            @Test
            @DisplayName("Should create payment transaction with negative amount - CRITICAL TEST")
            void shouldCreatePaymentTransactionWithNegativeAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 4L;
                BigDecimal inputAmount = new BigDecimal("-100.00");
                BigDecimal expectedAmount = new BigDecimal("100.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PAYMENT");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }

            @Test
            @DisplayName("Should create payment transaction with zero amount")
            void shouldCreatePaymentTransactionWithZeroAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 4L;
                BigDecimal inputAmount = new BigDecimal("0.00");
                BigDecimal expectedAmount = new BigDecimal("0.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PAYMENT");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }

            @Test
            @DisplayName("Should create payment transaction with decimal amount")
            void shouldCreatePaymentTransactionWithDecimalAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 4L;
                BigDecimal inputAmount = new BigDecimal("199.99");
                BigDecimal expectedAmount = new BigDecimal("199.99");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PAYMENT");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
                assertThat(result.getAmount()).isEqualByComparingTo(expectedAmount);
            }
        }

        @Nested
        @DisplayName("Event Date Tests")
        class EventDateTests {

            @Test
            @DisplayName("Should set event date to current timestamp")
            void shouldSetEventDateToCurrentTimestamp() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PURCHASE");
                mockTransactionRepositorySave();

                Instant before = Instant.now().minusSeconds(1);

                // Act
                transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                Instant after = Instant.now().plusSeconds(1);
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getEventDate())
                        .isNotNull()
                        .isAfter(before)
                        .isBefore(after);
            }

            @Test
            @DisplayName("Should set event date for all operation types")
            void shouldSetEventDateForAllOperationTypes() {
                // Test for each operation type
                for (Long operationTypeId = 1L; operationTypeId <= 4L; operationTypeId++) {
                    // Arrange
                    Long accountId = 1L;
                    BigDecimal inputAmount = new BigDecimal("100.00");
                    String description = "TYPE_" + operationTypeId;

                    mockAccountRepository(accountId, "12345678900");
                    mockOperationTypeRepository(operationTypeId, description);
                    mockTransactionRepositorySave();

                    // Act
                    transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                    // Assert
                    ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                    verify(transactionRepository, atLeastOnce()).save(captor.capture());
                    Transaction savedTransaction = captor.getValue();

                    assertThat(savedTransaction.getEventDate()).isNotNull();

                    // Reset mocks for next iteration
                    reset(accountRepository, operationTypeRepository, transactionRepository);
                }
            }
        }

        @Nested
        @DisplayName("Account Not Found Scenarios")
        class AccountNotFoundScenarios {

            @Test
            @DisplayName("Should throw AccountNotFoundException when account does not exist")
            void shouldThrowAccountNotFoundExceptionWhenAccountDoesNotExist() {
                // Arrange
                Long accountId = 999L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");

                when(accountRepository.findById(accountId))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() ->
                        transactionService.createTransaction(accountId, operationTypeId, inputAmount))
                        .isInstanceOf(AccountNotFoundException.class);

                verify(accountRepository, times(1)).findById(accountId);
                verify(operationTypeRepository, never()).findById(any());
                verify(transactionRepository, never()).save(any());
            }

            @Test
            @DisplayName("Should verify exception message when account not found")
            void shouldVerifyExceptionMessageWhenAccountNotFound() {
                // Arrange
                Long accountId = 999L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");

                when(accountRepository.findById(accountId))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() ->
                        transactionService.createTransaction(accountId, operationTypeId, inputAmount))
                        .isInstanceOf(AccountNotFoundException.class)
                        .hasMessage("Account with ID " + accountId + " not found.");

                verify(accountRepository, times(1)).findById(accountId);
            }
        }

        @Nested
        @DisplayName("Operation Type Not Found Scenarios")
        class OperationTypeNotFoundScenarios {

            @Test
            @DisplayName("Should throw OperationTypeNotFoundException when operation type does not exist")
            void shouldThrowOperationTypeNotFoundExceptionWhenOperationTypeDoesNotExist() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 999L;
                BigDecimal inputAmount = new BigDecimal("100.00");

                mockAccountRepository(accountId, "12345678900");
                when(operationTypeRepository.findById(operationTypeId))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() ->
                        transactionService.createTransaction(accountId, operationTypeId, inputAmount))
                        .isInstanceOf(OperationTypeNotFoundException.class);

                verify(accountRepository, times(1)).findById(accountId);
                verify(operationTypeRepository, times(1)).findById(operationTypeId);
                verify(transactionRepository, never()).save(any());
            }

            @Test
            @DisplayName("Should verify exception message when operation type not found")
            void shouldVerifyExceptionMessageWhenOperationTypeNotFound() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 999L;
                BigDecimal inputAmount = new BigDecimal("100.00");

                mockAccountRepository(accountId, "12345678900");
                when(operationTypeRepository.findById(operationTypeId))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() ->
                        transactionService.createTransaction(accountId, operationTypeId, inputAmount))
                        .isInstanceOf(OperationTypeNotFoundException.class)
                        .hasMessage("Operation type with ID " + operationTypeId + " not found.");

                verify(operationTypeRepository, times(1)).findById(operationTypeId);
            }
        }

        @Nested
        @DisplayName("Invalid Operation Type Code Scenarios")
        class InvalidOperationTypeCodeScenarios {

            @Test
            @DisplayName("Should throw IllegalArgumentException for invalid operation type")
            void shouldThrowIllegalArgumentExceptionForInvalidOperationType() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 5L; // Invalid operation type
                BigDecimal inputAmount = new BigDecimal("100.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "INVALID_TYPE");

                // Act & Assert
                assertThatThrownBy(() ->
                        transactionService.createTransaction(accountId, operationTypeId, inputAmount))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Invalid OperationType code: " + operationTypeId);

                verify(accountRepository, times(1)).findById(accountId);
                verify(operationTypeRepository, times(1)).findById(operationTypeId);
                verify(transactionRepository, never()).save(any());
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException for operation type zero")
            void shouldThrowIllegalArgumentExceptionForOperationTypeZero() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 0L; // Invalid operation type
                BigDecimal inputAmount = new BigDecimal("100.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "INVALID_TYPE");

                // Act & Assert
                assertThatThrownBy(() ->
                        transactionService.createTransaction(accountId, operationTypeId, inputAmount))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Invalid OperationType code: " + operationTypeId);

                verify(accountRepository, times(1)).findById(accountId);
                verify(operationTypeRepository, times(1)).findById(operationTypeId);
                verify(transactionRepository, never()).save(any());
            }
        }

        @Nested
        @DisplayName("Repository Interaction Tests")
        class RepositoryInteractionTests {

            @Test
            @DisplayName("Should call repositories in correct order")
            void shouldCallRepositoriesInCorrectOrder() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PURCHASE");
                mockTransactionRepositorySave();

                // Act
                transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                InOrder inOrder = inOrder(accountRepository, operationTypeRepository, transactionRepository);
                inOrder.verify(accountRepository).findById(accountId);
                inOrder.verify(operationTypeRepository).findById(operationTypeId);
                inOrder.verify(transactionRepository).save(any(Transaction.class));
            }

            @Test
            @DisplayName("Should not save transaction when account not found")
            void shouldNotSaveTransactionWhenAccountNotFound() {
                // Arrange
                Long accountId = 999L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");

                when(accountRepository.findById(accountId))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() ->
                        transactionService.createTransaction(accountId, operationTypeId, inputAmount))
                        .isInstanceOf(AccountNotFoundException.class);

                verify(transactionRepository, never()).save(any(Transaction.class));
            }

            @Test
            @DisplayName("Should not save transaction when operation type not found")
            void shouldNotSaveTransactionWhenOperationTypeNotFound() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 999L;
                BigDecimal inputAmount = new BigDecimal("100.00");

                mockAccountRepository(accountId, "12345678900");
                when(operationTypeRepository.findById(operationTypeId))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() ->
                        transactionService.createTransaction(accountId, operationTypeId, inputAmount))
                        .isInstanceOf(OperationTypeNotFoundException.class);

                verify(transactionRepository, never()).save(any(Transaction.class));
            }
        }

        @Nested
        @DisplayName("Entity Relationship Tests")
        class EntityRelationshipTests {

            @Test
            @DisplayName("Should set correct account in transaction")
            void shouldSetCorrectAccountInTransaction() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");
                String documentNumber = "12345678900";

                mockAccountRepository(accountId, documentNumber);
                mockOperationTypeRepository(operationTypeId, "PURCHASE");
                mockTransactionRepositorySave();

                // Act
                transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAccount()).isNotNull();
                assertThat(savedTransaction.getAccount().getAccountId()).isEqualTo(accountId);
                assertThat(savedTransaction.getAccount().getDocumentNumber()).isEqualTo(documentNumber);
            }

            @Test
            @DisplayName("Should set correct operation type in transaction")
            void shouldSetCorrectOperationTypeInTransaction() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");
                String description = "PURCHASE";

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, description);
                mockTransactionRepositorySave();

                // Act
                transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getOperationType()).isNotNull();
                assertThat(savedTransaction.getOperationType().getOperationTypeId()).isEqualTo(operationTypeId);
                assertThat(savedTransaction.getOperationType().getDescription()).isEqualTo(description);
            }

            @Test
            @DisplayName("Should build complete transaction entity")
            void shouldBuildCompleteTransactionEntity() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PURCHASE");
                mockTransactionRepositorySave();

                // Act
                transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAccount()).isNotNull();
                assertThat(savedTransaction.getOperationType()).isNotNull();
                assertThat(savedTransaction.getAmount()).isNotNull();
                assertThat(savedTransaction.getEventDate()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Edge Cases")
        class EdgeCases {

            @Test
            @DisplayName("Should handle null account ID")
            void shouldHandleNullAccountId() {
                // Arrange
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");

                when(accountRepository.findById(null))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() ->
                        transactionService.createTransaction(null, operationTypeId, inputAmount))
                        .isInstanceOf(AccountNotFoundException.class);

                verify(accountRepository, times(1)).findById(null);
            }

            @Test
            @DisplayName("Should handle null operation type ID")
            void shouldHandleNullOperationTypeId() {
                // Arrange
                Long accountId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");

                mockAccountRepository(accountId, "12345678900");
                when(operationTypeRepository.findById(null))
                        .thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() ->
                        transactionService.createTransaction(accountId, null, inputAmount))
                        .isInstanceOf(OperationTypeNotFoundException.class);

                verify(operationTypeRepository, times(1)).findById(null);
            }

            @Test
            @DisplayName("Should handle null amount")
            void shouldHandleNullAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PURCHASE");

                // Act & Assert
                assertThatThrownBy(() ->
                        transactionService.createTransaction(accountId, operationTypeId, null))
                        .isInstanceOf(NullPointerException.class);
            }

            @Test
            @DisplayName("Should handle very large amount")
            void shouldHandleVeryLargeAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("999999999.99");
                BigDecimal expectedAmount = new BigDecimal("-999999999.99");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PURCHASE");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
            }

            @Test
            @DisplayName("Should handle very small decimal amount")
            void shouldHandleVerySmallDecimalAmount() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("0.01");
                BigDecimal expectedAmount = new BigDecimal("-0.01");

                mockAccountRepository(accountId, "12345678900");
                mockOperationTypeRepository(operationTypeId, "PURCHASE");
                mockTransactionRepositorySave();

                // Act
                Transaction result = transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
                verify(transactionRepository).save(captor.capture());
                Transaction savedTransaction = captor.getValue();

                assertThat(savedTransaction.getAmount()).isEqualByComparingTo(expectedAmount);
            }
        }

        @Nested
        @DisplayName("Credit Limit Tests")
        class CreditLimitTests {

            @Test
            @DisplayName("Should update account credit limit after purchase transaction")
            void shouldUpdateAccountCreditLimitAfterPurchaseTransaction() {
                // Arrange
                Long accountId = 1L;
                Long operationTypeId = 1L;
                BigDecimal inputAmount = new BigDecimal("100.00");
                BigDecimal initialCreditLimit = new BigDecimal("500.00");
                BigDecimal expectedCreditLimit = new BigDecimal("400.00");

                mockAccountRepository(accountId, "12345678900", initialCreditLimit);
                mockOperationTypeRepository(operationTypeId, "PURCHASE");
                mockTransactionRepositorySave();

                // Act
                transactionService.createTransaction(accountId, operationTypeId, inputAmount);

                // Assert
                ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
                verify(accountRepository).save(accountCaptor.capture());
                Account savedAccount = accountCaptor.getValue();

                assertThat(savedAccount.getAvailableCreditLimit()).isEqualByComparingTo(expectedCreditLimit);
            }
        }
    }
}
