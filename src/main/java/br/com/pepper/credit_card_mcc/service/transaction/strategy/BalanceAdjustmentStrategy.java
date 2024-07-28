package br.com.pepper.credit_card_mcc.service.transaction.strategy;

import java.math.BigDecimal;

import br.com.pepper.credit_card_mcc.model.account.Account;

public interface BalanceAdjustmentStrategy {
    void adjustBalance(Account account, BigDecimal amount);
}
