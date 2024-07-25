package br.com.pepper.credit_card_mcc.model;

import br.com.pepper.credit_card_mcc.enums.TransactionStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionTest {

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction.Builder()
                .accountId(1L)
                .createdOn(LocalDateTime.now())
                .amount(new BigDecimal("100.00"))
                .merchantName("Test Merchant")
                .mcc("5812")
                .status(TransactionStatusEnum.APPROVED)
                .build();
    }

    @Test
    void testTransactionBuilder() {
        assertNotNull(transaction);
        assertEquals(1L, transaction.getAccountId());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertEquals("Test Merchant", transaction.getMerchantName());
        assertEquals("5812", transaction.getMcc());
        assertEquals(TransactionStatusEnum.APPROVED, transaction.getStatus());
        assertNotNull(transaction.getCreatedOn());
    }

    @Test
    void testTransactionFields() {
        Transaction customTransaction = new Transaction.Builder()
                .id(1L)
                .accountId(2L)
                .createdOn(LocalDateTime.of(2024, 7, 24, 23, 31))
                .amount(new BigDecimal("200.00"))
                .merchantName("John Doo Merchant")
                .mcc("5811")
                .status(TransactionStatusEnum.DECLINED)
                .build();

        assertEquals(1L, customTransaction.getId());
        assertEquals(2L, customTransaction.getAccountId());
        assertEquals(LocalDateTime.of(2024, 7, 24, 23, 31), customTransaction.getCreatedOn());
        assertEquals(new BigDecimal("200.00"), customTransaction.getAmount());
        assertEquals("John Doo Merchant", customTransaction.getMerchantName());
        assertEquals("5811", customTransaction.getMcc());
        assertEquals(TransactionStatusEnum.DECLINED, customTransaction.getStatus());
    }
}
