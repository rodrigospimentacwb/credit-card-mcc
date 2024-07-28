package br.com.pepper.credit_card_mcc.controller.transaction;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pepper.credit_card_mcc.controller.transaction.dto.RequestCreateTransactionDto;
import br.com.pepper.credit_card_mcc.controller.transaction.dto.ResponseCreateTransactionDto;
import br.com.pepper.credit_card_mcc.controller.transaction.facade.TransactionFacade;

@RestController
@RequestMapping(path = "/transactions/v1")
public class TransactionController {

    @Resource
    private TransactionFacade facade;

    @PostMapping
    public ResponseCreateTransactionDto createTransaction(@Valid @RequestBody RequestCreateTransactionDto request) {
        return facade.createTransaction(request);
    }
}
