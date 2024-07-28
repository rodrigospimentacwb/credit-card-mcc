package br.com.pepper.credit_card_mcc.controller.transaction.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pepper.credit_card_mcc.controller.transaction.dto.RequestCreateTransactionDto;
import br.com.pepper.credit_card_mcc.controller.transaction.dto.ResponseCreateTransactionDto;
import br.com.pepper.credit_card_mcc.enums.TransactionStatusType;
import br.com.pepper.credit_card_mcc.exception.CustomException;
import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.service.account.AccountService;
import br.com.pepper.credit_card_mcc.service.transaction.TransactionService;
import br.com.pepper.credit_card_mcc.utils.AbstractTestUtils;

@ExtendWith(MockitoExtension.class)
class TransactionFacadeTest extends AbstractTestUtils {

    RequestCreateTransactionDto request;
    Account account;

    @BeforeEach
    void setUp() {
        request = new RequestCreateTransactionDto("1", BigDecimal.TEN, "1", "1");
        account = new Account(1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);

        setupLogger(TransactionFacade.class);
    }

    @AfterEach
    public void tearDown() {
        clearLogger(TransactionFacade.class);
    }

    @Test
    void createTransaction() {
        when(accountService.findAccountByIdWithValidation(anyLong())).thenReturn(account);
        when(transactionService.createTransaction(any(Account.class), any(BigDecimal.class), any(String.class), any(String.class))).thenReturn(TransactionStatusType.APPROVED);

        ResponseCreateTransactionDto response = facade.createTransaction(request);

        assertNotNull(response);
        assertEquals(TransactionStatusType.APPROVED.getCode(), response.code());
        verify(accountService).findAccountByIdWithValidation(Long.valueOf(request.account()));
        verify(transactionService).createTransaction(account, request.totalAmount(), request.mcc(), request.merchant());
    }

    @Test
    void createTransactionAccountNotFound() {
        when(accountService.findAccountByIdWithValidation(anyLong())).thenThrow(new CustomException("Account not found"));

        ResponseCreateTransactionDto response = facade.createTransaction(request);

        assertNotNull(response);
        assertLogContains("Error creating transaction");
    }

    @Test
    void createTransactionWithExceptionInTransactionService() {
        when(accountService.findAccountByIdWithValidation(anyLong())).thenReturn(account);
        when(transactionService.createTransaction(any(Account.class), any(BigDecimal.class), any(String.class), any(String.class))).thenThrow(new CustomException("Error creating transaction"));

        ResponseCreateTransactionDto response = facade.createTransaction(request);

        assertNotNull(response);
        assertLogContains("Error creating transaction");
    }

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionFacade facade;
}