package br.com.pepper.credit_card_mcc.service.transaction.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.exception.InsufficientBalanceException;
import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.utils.AbstractTestUtils;

class MealBalanceAdjustmentStrategyTest extends AbstractTestUtils {

    Account account;

    @Mock
    private CashBalanceAdjustmentStrategy cashBalanceAdjustmentStrategy;

    @InjectMocks
    private MealBalanceAdjustmentStrategy mealBalanceAdjustmentStrategy;

    @BeforeEach
    void setUp() {
        account = new Account(1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
    }

    @Test
    void adjustBalance() {
        mealBalanceAdjustmentStrategy.adjustBalance(account, BigDecimal.ONE);
        assertEquals(BigDecimal.valueOf(9), account.getMealBalance());
    }

    @Test
    void adjustBalanceWithDecimal() {
        mealBalanceAdjustmentStrategy.adjustBalance(account, BigDecimal.valueOf(5.42));
        assertEquals(BigDecimal.valueOf(4.58), account.getMealBalance());
    }

    @Test
    void adjustBalanceWithoutBalanceButWithCashBalance() {
        BigDecimal amount = BigDecimal.valueOf(5);
        Account customAccount = new Account(1L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.TEN);

        doCallRealMethod().when(cashBalanceAdjustmentStrategy).adjustBalance(any(Account.class), any(BigDecimal.class));

        mealBalanceAdjustmentStrategy.adjustBalance(customAccount, amount);

        assertEquals(BigDecimal.ZERO, customAccount.getMealBalance());
        assertEquals(BigDecimal.valueOf(5), customAccount.getCashBalance());
        assertEquals(BigDecimal.ZERO, customAccount.getFoodBalance());
        assertEquals(MccType.CASH, customAccount.getRealMccTypeApplied());
    }

    @Test
    void ajustBalanceWithoutBalanceAndCashBalance() {
        BigDecimal amount = BigDecimal.valueOf(5);
        Account customAccount = new Account(1L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        doCallRealMethod().when(cashBalanceAdjustmentStrategy).adjustBalance(any(Account.class), any(BigDecimal.class));

        InsufficientBalanceException thrown = assertThrows(InsufficientBalanceException.class, () ->
                mealBalanceAdjustmentStrategy.adjustBalance(customAccount, amount)
        );
        String expectedMessage = String.format("Insufficient balance in account '%s' for the requested amount of %s to MCC %s.", customAccount.getId(), -5L, MccType.CASH);
        assertEquals(expectedMessage, thrown.getMessage());

        assertEquals(BigDecimal.ZERO, customAccount.getFoodBalance());
        assertEquals(BigDecimal.ZERO, customAccount.getCashBalance());
        assertEquals(BigDecimal.ZERO, customAccount.getMealBalance());
    }
}