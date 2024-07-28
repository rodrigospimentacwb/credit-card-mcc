package br.com.pepper.credit_card_mcc.controller.transaction.facade;

import static br.com.pepper.credit_card_mcc.enums.TransactionStatusType.DECLINED;
import static br.com.pepper.credit_card_mcc.enums.TransactionStatusType.FAILED;

import jakarta.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.pepper.credit_card_mcc.controller.transaction.dto.RequestCreateTransactionDto;
import br.com.pepper.credit_card_mcc.controller.transaction.dto.ResponseCreateTransactionDto;
import br.com.pepper.credit_card_mcc.enums.TransactionStatusType;
import br.com.pepper.credit_card_mcc.exception.InsufficientBalanceException;
import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.service.account.AccountService;
import br.com.pepper.credit_card_mcc.service.transaction.TransactionService;

@Component
public class TransactionFacade {

    Logger logger = LoggerFactory.getLogger(TransactionFacade.class);

    @Resource
    private TransactionService transactionService;

    @Resource
    private AccountService accountService;

    public ResponseCreateTransactionDto createTransaction(RequestCreateTransactionDto request) {
        try {
            Account account = accountService.findAccountByIdWithValidation(Long.valueOf(request.account()));
            TransactionStatusType status = transactionService.createTransaction(account, request.totalAmount(), request.mcc(), request.merchant());
            return buildResponse(status);
        } catch (InsufficientBalanceException e) {
            logger.error("Error creating transaction", e);
            return buildResponse(DECLINED);
        } catch (Exception e) {
            logger.error("Error creating transaction", e);
            return buildResponse(FAILED);
        }
    }

    private ResponseCreateTransactionDto buildResponse(TransactionStatusType status) {
        return new ResponseCreateTransactionDto(status.getCode());
    }
}
