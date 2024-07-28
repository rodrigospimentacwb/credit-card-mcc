package br.com.pepper.credit_card_mcc.service.transaction.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.exception.InsufficientBalanceException;
import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.utils.AbstractTestUtils;

class CashBalanceAdjustmentStrategyTest extends AbstractTestUtils {

    Account account;

    @InjectMocks
    private CashBalanceAdjustmentStrategy cashBalanceAdjustmentStrategy;

    @BeforeEach
    void setUp() {
        account = new Account(1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
    }

    @Test
    void adjustBalance() {
        cashBalanceAdjustmentStrategy.adjustBalance(account, BigDecimal.ONE);
        assertEquals(BigDecimal.valueOf(9), account.getCashBalance());
    }

    @Test
    void adjustBalanceWithDecimal() {
        cashBalanceAdjustmentStrategy.adjustBalance(account, BigDecimal.valueOf(5.42));
        assertEquals(BigDecimal.valueOf(4.58), account.getCashBalance());
    }

    @Test
    void adjustBalanceWithoutBalance() {
        BigDecimal amount = BigDecimal.valueOf(11);
        InsufficientBalanceException thrown = assertThrows(InsufficientBalanceException.class, () ->
                cashBalanceAdjustmentStrategy.adjustBalance(account, amount)
        );
        String expectedMessage = String.format("Insufficient balance in account '%s' for the requested amount of %s to MCC %s.", account.getId(), -1L, MccType.CASH);
        assertEquals(expectedMessage, thrown.getMessage());

        assertEquals(BigDecimal.TEN, account.getFoodBalance());
        assertEquals(BigDecimal.TEN, account.getCashBalance());
        assertEquals(BigDecimal.TEN, account.getMealBalance());
    }
}