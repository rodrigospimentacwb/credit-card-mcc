package br.com.pepper.credit_card_mcc.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.pepper.credit_card_mcc.controller.transaction.dto.ResponseCreateTransactionDto;
import br.com.pepper.credit_card_mcc.enums.TransactionStatusType;

@RestControllerAdvice
public class AuthGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthGlobalExceptionHandler.class);
    private static final ResponseCreateTransactionDto DEFAULT_ERROR_RESPONSE =
            new ResponseCreateTransactionDto(TransactionStatusType.FAILED.getCode());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseCreateTransactionDto> handleException(Exception exception) {
        logger.error("Error creating transaction", exception);
        return new ResponseEntity<>(DEFAULT_ERROR_RESPONSE, HttpStatus.OK);
    }
}
