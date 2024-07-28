package br.com.pepper.credit_card_mcc.model.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.exception.InsufficientBalanceException;
import br.com.pepper.credit_card_mcc.utils.AbstractTestUtils;

class AccountTest extends AbstractTestUtils {

    @Test
    void acountCreation() {
        Long id = 1L;
        BigDecimal foodBalance = new BigDecimal("100.00");
        BigDecimal mealBalance = new BigDecimal("200.00");
        BigDecimal cashBalance = new BigDecimal("300.00");

        Account account = new Account(id, foodBalance, mealBalance, cashBalance);

        assertEquals(id, account.getId());
        assertEquals(foodBalance, account.getFoodBalance());
        assertEquals(mealBalance, account.getMealBalance());
        assertEquals(cashBalance, account.getCashBalance());
    }

    @Test
    void accountDefaultConstructor() {
        Account account = new Account();

        assertNull(account.getId());
        assertNull(account.getFoodBalance());
        assertNull(account.getMealBalance());
        assertNull(account.getCashBalance());
    }

    @ParameterizedTest
    @CsvSource({ MCC_FOOD_CODE_1, MCC_FOOD_CODE_2, MCC_MEAL_CODE_1, MCC_MEAL_CODE_2, MCC_CASH_CODE })
    void applyNewBalance(String mcc) {
        MccType mccType = MccType.fromMcc(mcc);

        Account account = new Account(1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
        BigDecimal newBalance = new BigDecimal("1.42");

        switch (mccType) {
            case FOOD -> {
                account.applyFoodBalance(newBalance);
                assertEquals(newBalance, account.getFoodBalance());
            }
            case MEAL -> {
                account.applyMealBalance(newBalance);
                assertEquals(newBalance, account.getMealBalance());
            }
            case CASH -> {
                account.applyCashBalance(newBalance);
                assertEquals(newBalance, account.getCashBalance());
            }
        }
    }

    @ParameterizedTest
    @CsvSource({ MCC_FOOD_CODE_1, MCC_FOOD_CODE_2, MCC_MEAL_CODE_1, MCC_MEAL_CODE_2, MCC_CASH_CODE })
    void applyNewBalanceWithoutBalance(String mcc) {
        String expectedMessage = "";
        InsufficientBalanceException thrown = null;
        MccType mccType = MccType.fromMcc(mcc);

        Account account = new Account(1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
        BigDecimal newBalance = new BigDecimal("-0.01");

        switch (mccType) {
            case FOOD -> {
                thrown = assertThrows(InsufficientBalanceException.class, () ->
                        account.applyFoodBalance(newBalance)
                );
                expectedMessage = String.format("Insufficient balance in account '%s' for the requested amount of %s to MCC %s.", account.getId(), newBalance, mccType);
            }
            case MEAL -> {
                thrown = assertThrows(InsufficientBalanceException.class, () ->
                        account.applyMealBalance(newBalance)
                );
                expectedMessage = String.format("Insufficient balance in account '%s' for the requested amount of %s to MCC %s.", account.getId(), newBalance, mccType);
            }
            case CASH -> {
                thrown = assertThrows(InsufficientBalanceException.class, () ->
                        account.applyCashBalance(newBalance)
                );
                expectedMessage = String.format("Insufficient balance in account '%s' for the requested amount of %s to MCC %s.", account.getId(), newBalance, mccType);
            }
        }
        assertEquals(expectedMessage, thrown.getMessage());
    }
}
