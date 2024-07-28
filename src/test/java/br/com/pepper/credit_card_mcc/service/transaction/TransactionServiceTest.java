package br.com.pepper.credit_card_mcc.service.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.enums.TransactionStatusType;
import br.com.pepper.credit_card_mcc.exception.InsufficientBalanceException;
import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.model.transaction.Transaction;
import br.com.pepper.credit_card_mcc.repository.transaction.TransactionRepository;
import br.com.pepper.credit_card_mcc.service.account.AccountService;
import br.com.pepper.credit_card_mcc.service.transaction.strategy.BalanceAdjustmentContext;
import br.com.pepper.credit_card_mcc.utils.AbstractTestUtils;

class TransactionServiceTest extends AbstractTestUtils {

    private BigDecimal amount;
    private Account account;
    private Transaction transaction;

    @Mock
    private BalanceAdjustmentContext balanceAdjustmentContext;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService service;

    @BeforeEach
    void setUp() {
        amount = BigDecimal.TEN;
        account = new Account(1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
        transaction = new Transaction.Builder().account(account).amount(amount).mcc(MccType.FOOD).status(TransactionStatusType.APPROVED).build();
    }

    @ParameterizedTest
    @CsvSource({ MCC_FOOD_CODE_1, MCC_FOOD_CODE_2, MCC_MEAL_CODE_1, MCC_MEAL_CODE_2, MCC_CASH_CODE })
    void createTransactionApproved(String mcc) {
        MccType mccType = MccType.fromMcc(mcc);

        doNothing().when(balanceAdjustmentContext).adjustBalance(any(MccType.class), any(Account.class), any(BigDecimal.class));
        Mockito.when(accountService.update(any(Account.class))).thenReturn(account);
        Mockito.when(repository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionStatusType status = service.createTransaction(account, amount, mcc, MERCHANT_NAME);

        assertSame(TransactionStatusType.APPROVED, status);

        verify(balanceAdjustmentContext).adjustBalance(mccType, account, amount);
        verify(accountService).update(any(Account.class));
        verify(repository).save(any(Transaction.class));
    }

    @ParameterizedTest
    @CsvSource({ MCC_FOOD_CODE_1, MCC_FOOD_CODE_2, MCC_MEAL_CODE_1, MCC_MEAL_CODE_2, MCC_CASH_CODE })
    void createTransactionDeclined(String mcc) {
        MccType mccType = MccType.fromMcc(mcc);

        doThrow(new InsufficientBalanceException(account, amount, mccType)).when(balanceAdjustmentContext).adjustBalance(any(MccType.class), any(Account.class), any(BigDecimal.class));
        Mockito.when(accountService.update(any(Account.class))).thenReturn(account);
        Mockito.when(repository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionStatusType status = service.createTransaction(account, amount, mcc, MERCHANT_NAME);

        assertSame(TransactionStatusType.DECLINED, status);

        verify(balanceAdjustmentContext).adjustBalance(mccType, account, amount);
        verify(accountService).update(any(Account.class));
        verify(repository).save(any(Transaction.class));
    }

    @ParameterizedTest
    @CsvSource({ MCC_FOOD_CODE_1, MCC_FOOD_CODE_2, MCC_MEAL_CODE_1, MCC_MEAL_CODE_2, MCC_CASH_CODE })
    void createTransactionPersistenceAccountFail(String mcc) {
        MccType mccType = MccType.fromMcc(mcc);

        doNothing().when(balanceAdjustmentContext).adjustBalance(any(MccType.class), any(Account.class), any(BigDecimal.class));
        Mockito.when(accountService.update(any(Account.class))).thenThrow(new RuntimeException("Fail persist Account"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                service.createTransaction(account, amount, mcc, MERCHANT_NAME)
        );
        assertEquals("Fail persist Account", thrown.getMessage());

        verify(balanceAdjustmentContext).adjustBalance(mccType, account, amount);
        verify(accountService).update(any(Account.class));
    }

    @ParameterizedTest
    @CsvSource({ MCC_FOOD_CODE_1, MCC_FOOD_CODE_2, MCC_MEAL_CODE_1, MCC_MEAL_CODE_2, MCC_CASH_CODE })
    void createTransactionPersistenceTransactionFail(String mcc) {
        MccType mccType = MccType.fromMcc(mcc);

        doNothing().when(balanceAdjustmentContext).adjustBalance(any(MccType.class), any(Account.class), any(BigDecimal.class));
        Mockito.when(accountService.update(any(Account.class))).thenThrow(new RuntimeException("Fail persist Transaction"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                service.createTransaction(account, amount, mcc, MERCHANT_NAME)
        );
        assertEquals("Fail persist Transaction", thrown.getMessage());

        verify(balanceAdjustmentContext).adjustBalance(mccType, account, amount);
        verify(accountService).update(any(Account.class));
    }
}