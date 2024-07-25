package br.com.pepper.credit_card_mcc.repository;

import br.com.pepper.credit_card_mcc.model.Account;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AccountRepositoryTest {

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

    @Resource
    private AccountRepository repository;
}