package br.com.pepper.credit_card_mcc.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class AccountTest {

    @Test
    void testAccountCreation() {
        // Given
        Long id = 1L;
        BigDecimal foodBalance = new BigDecimal("100.00");
        BigDecimal mealBalance = new BigDecimal("200.00");
        BigDecimal cashBalance = new BigDecimal("300.00");

        // When
        Account account = new Account(id, foodBalance, mealBalance, cashBalance);

        // Then
        assertEquals(id, account.getId());
        assertEquals(foodBalance, account.getFoodBalance());
        assertEquals(mealBalance, account.getMealBalance());
        assertEquals(cashBalance, account.getCashBalance());
    }

    @Test
    void testAccountDefaultConstructor() {
        // When
        Account account = new Account();

        // Then
        assertNull(account.getId());
        assertNull(account.getFoodBalance());
        assertNull(account.getMealBalance());
        assertNull(account.getCashBalance());
    }
}
