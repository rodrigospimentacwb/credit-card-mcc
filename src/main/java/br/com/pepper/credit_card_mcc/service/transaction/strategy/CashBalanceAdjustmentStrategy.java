package br.com.pepper.credit_card_mcc.service.transaction.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.com.pepper.credit_card_mcc.model.account.Account;

@Service
public class CashBalanceAdjustmentStrategy implements BalanceAdjustmentStrategy {

    @Override
    public void adjustBalance(Account account, BigDecimal amount) {
        BigDecimal newBalance = account.getCashBalance().subtract(amount);
        account.applyCashBalance(newBalance);
    }
}
