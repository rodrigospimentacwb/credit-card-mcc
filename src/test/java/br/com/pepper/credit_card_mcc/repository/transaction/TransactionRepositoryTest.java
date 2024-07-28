package br.com.pepper.credit_card_mcc.repository.transaction;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.annotation.Resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.enums.TransactionStatusType;
import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.model.transaction.Transaction;
import br.com.pepper.credit_card_mcc.repository.account.AccountRepository;
import br.com.pepper.credit_card_mcc.utils.AbstractRepositoryTestUtils;

class TransactionRepositoryTest extends AbstractRepositoryTestUtils {

    Account account;

    @Resource
    private TransactionRepository repository;

    @Resource
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        account = new Account(null, new BigDecimal("100.00"), new BigDecimal("200.00"), new BigDecimal("300.00"));
    }

    @Test
    void findById() {
        account = accountRepository.save(account);

        LocalDateTime now = now();
        Transaction transaction = new Transaction.Builder()
                .account(account)
                .createdOn(now)
                .amount(BigDecimal.valueOf(100.00))
                .merchantName("Test Merchant")
                .mcc(MccType.CASH)
                .status(TransactionStatusType.APPROVED)
                .build();

        Transaction savedTransaction = repository.save(transaction);

        Optional<Transaction> foundTransaction = repository.findById(savedTransaction.getId());

        assertTrue(foundTransaction.isPresent());
        Transaction actualTransaction = foundTransaction.get();

        assertEquals(transaction.getAccount().getId(), actualTransaction.getAccount().getId());
        assertEquals(transaction.getAmount(), actualTransaction.getAmount());
        assertEquals(transaction.getMerchantName(), actualTransaction.getMerchantName());
        assertEquals(transaction.getMcc(), actualTransaction.getMcc());
        assertEquals(transaction.getStatus(), actualTransaction.getStatus());
        assertTrue(transaction.getCreatedOn().isEqual(now));
    }
}