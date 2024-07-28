package br.com.pepper.credit_card_mcc.controller.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import br.com.pepper.credit_card_mcc.controller.transaction.dto.RequestCreateTransactionDto;
import br.com.pepper.credit_card_mcc.controller.transaction.dto.ResponseCreateTransactionDto;
import br.com.pepper.credit_card_mcc.controller.transaction.facade.TransactionFacade;
import br.com.pepper.credit_card_mcc.enums.TransactionStatusType;
import br.com.pepper.credit_card_mcc.utils.AbstractTestUtils;

class TransactionControllerTest extends AbstractTestUtils {

    RequestCreateTransactionDto request;
    ResponseCreateTransactionDto response;

    @BeforeEach
    void setUp() {
        request = new RequestCreateTransactionDto("1", BigDecimal.TEN, "1", "1");
        response = new ResponseCreateTransactionDto(TransactionStatusType.APPROVED.getCode());
    }

    @Test
    void createTransactionApprove() {
        when(facade.createTransaction(any(RequestCreateTransactionDto.class))).thenReturn(response);

        ResponseCreateTransactionDto expectedResponse = transactionController.createTransaction(request);
        assertNotNull(expectedResponse);
        assertEquals(TransactionStatusType.APPROVED.getCode(), expectedResponse.code());
        verify(facade).createTransaction(any(RequestCreateTransactionDto.class));
    }

    @Test
    void createTransactionDecline() {
        response = new ResponseCreateTransactionDto(TransactionStatusType.DECLINED.getCode());
        when(facade.createTransaction(any(RequestCreateTransactionDto.class))).thenReturn(response);

        ResponseCreateTransactionDto expectedResponse = transactionController.createTransaction(request);
        assertNotNull(expectedResponse);
        assertEquals(TransactionStatusType.DECLINED.getCode(), expectedResponse.code());
        verify(facade).createTransaction(any(RequestCreateTransactionDto.class));
    }

    @Test
    void createTransactionFailed() {
        response = new ResponseCreateTransactionDto(TransactionStatusType.FAILED.getCode());
        when(facade.createTransaction(any(RequestCreateTransactionDto.class))).thenReturn(response);

        ResponseCreateTransactionDto expectedResponse = transactionController.createTransaction(request);
        assertNotNull(expectedResponse);
        assertEquals(TransactionStatusType.FAILED.getCode(), expectedResponse.code());
        verify(facade).createTransaction(any(RequestCreateTransactionDto.class));
    }

    @Mock
    private TransactionFacade facade;

    @InjectMocks
    private TransactionController transactionController;
}