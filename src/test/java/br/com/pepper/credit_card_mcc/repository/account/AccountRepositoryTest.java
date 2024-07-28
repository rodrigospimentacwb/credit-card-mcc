package br.com.pepper.credit_card_mcc.repository.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;

import jakarta.annotation.Resource;

import org.junit.jupiter.api.Test;

import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.utils.AbstractRepositoryTestUtils;

class AccountRepositoryTest extends AbstractRepositoryTestUtils {

    @Resource
    private AccountRepository repository;

    @Test
    void findById() {
        Account account = new Account(1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
        repository.save(account);

        Optional<Account> accountFound = repository.findById(1L);

        assertTrue(accountFound.isPresent());
        assertEquals(BigDecimal.TEN, accountFound.get().getCashBalance());
        assertEquals(BigDecimal.TEN, accountFound.get().getFoodBalance());
        assertEquals(BigDecimal.TEN, accountFound.get().getMealBalance());
    }
}