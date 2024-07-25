package br.com.pepper.credit_card_mcc.repository;

import br.com.pepper.credit_card_mcc.enums.TransactionStatusEnum;
import br.com.pepper.credit_card_mcc.model.Transaction;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Test
    void findById() {
        LocalDateTime now = now();
        Transaction transaction = new Transaction.Builder()
                .accountId(123L)
                .createdOn(now)
                .amount(BigDecimal.valueOf(100.00))
                .merchantName("Test Merchant")
                .mcc("1234")
                .status(TransactionStatusEnum.APPROVED)
                .build();

        Transaction savedTransaction = repository.save(transaction);

        Optional<Transaction> foundTransaction = repository.findById(savedTransaction.getId());

        assertTrue(foundTransaction.isPresent());
        Transaction actualTransaction = foundTransaction.get();

        assertEquals(transaction.getAccountId(), actualTransaction.getAccountId());
        assertEquals(transaction.getAmount(), actualTransaction.getAmount());
        assertEquals(transaction.getMerchantName(), actualTransaction.getMerchantName());
        assertEquals(transaction.getMcc(), actualTransaction.getMcc());
        assertEquals(transaction.getStatus(), actualTransaction.getStatus());
        assertTrue(transaction.getCreatedOn().isEqual(now));
    }

    @Resource
    private TransactionRepository repository;
}