package br.com.pepper.credit_card_mcc.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.enums.TransactionStatusType;
import br.com.pepper.credit_card_mcc.model.account.Account;

class TransactionTest {

    private Transaction transaction;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(1L, new BigDecimal("100.00"), new BigDecimal("200.00"), new BigDecimal("300.00"));

        transaction = new Transaction.Builder()
                .account(account)
                .createdOn(LocalDateTime.now())
                .amount(new BigDecimal("100.00"))
                .merchantName("Test Merchant")
                .mcc(MccType.fromMcc("5812"))
                .status(TransactionStatusType.APPROVED)
                .build();
    }

    @Test
    void transactionBuilder() {
        assertNotNull(transaction);
        assertEquals(1L, transaction.getAccount().getId());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertEquals("Test Merchant", transaction.getMerchantName());
        assertEquals(MccType.fromMcc("5812"), transaction.getMcc());
        assertEquals(TransactionStatusType.APPROVED, transaction.getStatus());
        assertNotNull(transaction.getCreatedOn());
    }

    @Test
    void transactionFields() {
        Transaction customTransaction = new Transaction.Builder()
                .id(1L)
                .account(account)
                .createdOn(LocalDateTime.of(2024, 7, 24, 23, 31))
                .amount(new BigDecimal("200.00"))
                .merchantName("John Doo Merchant")
                .mcc(MccType.fromMcc("5811"))
                .status(TransactionStatusType.DECLINED)
                .build();

        assertEquals(1L, customTransaction.getId());
        assertEquals(1L, customTransaction.getAccount().getId());
        assertEquals(LocalDateTime.of(2024, 7, 24, 23, 31), customTransaction.getCreatedOn());
        assertEquals(new BigDecimal("200.00"), customTransaction.getAmount());
        assertEquals("John Doo Merchant", customTransaction.getMerchantName());
        assertEquals(MccType.fromMcc("5811"), customTransaction.getMcc());
        assertEquals(TransactionStatusType.DECLINED, customTransaction.getStatus());
    }
}
