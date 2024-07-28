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

class FoodBalanceAdjustmentStrategyTest extends AbstractTestUtils {

    Account account;

    @Mock
    private CashBalanceAdjustmentStrategy cashBalanceAdjustmentStrategy;

    @InjectMocks
    private FoodBalanceAdjustmentStrategy foodBalanceAdjustmentStrategy;

    @BeforeEach
    void setUp() {
        account = new Account(1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
    }

    @Test
    void adjustBalance() {
        foodBalanceAdjustmentStrategy.adjustBalance(account, BigDecimal.ONE);
        assertEquals(BigDecimal.valueOf(9), account.getFoodBalance());
    }

    @Test
    void adjustBalanceWithDecimal() {
        foodBalanceAdjustmentStrategy.adjustBalance(account, BigDecimal.valueOf(5.42));
        assertEquals(BigDecimal.valueOf(4.58), account.getFoodBalance());
    }

    @Test
    void adjustBalanceWithoutBalanceButWithCashBalance() {
        BigDecimal amount = BigDecimal.valueOf(5);
        Account customAccount = new Account(1L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.TEN);

        doCallRealMethod().when(cashBalanceAdjustmentStrategy).adjustBalance(any(Account.class), any(BigDecimal.class));

        foodBalanceAdjustmentStrategy.adjustBalance(customAccount, amount);

        assertEquals(BigDecimal.ZERO, customAccount.getFoodBalance());
        assertEquals(BigDecimal.valueOf(5), customAccount.getCashBalance());
        assertEquals(BigDecimal.ZERO, customAccount.getMealBalance());
    }

    @Test
    void adjustBalanceWithoutBalanceAndCashBalance() {
        BigDecimal amount = BigDecimal.valueOf(5);
        Account customAccount = new Account(1L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        doCallRealMethod().when(cashBalanceAdjustmentStrategy).adjustBalance(any(Account.class), any(BigDecimal.class));

        InsufficientBalanceException thrown = assertThrows(InsufficientBalanceException.class, () ->
                foodBalanceAdjustmentStrategy.adjustBalance(customAccount, amount)
        );
        String expectedMessage = String.format("Insufficient balance in account '%s' for the requested amount of %s to MCC %s.", customAccount.getId(), -5L, MccType.CASH);
        assertEquals(expectedMessage, thrown.getMessage());

        assertEquals(BigDecimal.ZERO, customAccount.getFoodBalance());
        assertEquals(BigDecimal.ZERO, customAccount.getCashBalance());
        assertEquals(BigDecimal.ZERO, customAccount.getMealBalance());
    }
}