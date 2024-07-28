package br.com.pepper.credit_card_mcc.service.transaction.strategy;

import java.math.BigDecimal;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.pepper.credit_card_mcc.exception.InsufficientBalanceException;
import br.com.pepper.credit_card_mcc.model.account.Account;

@Service
public class MealBalanceAdjustmentStrategy implements BalanceAdjustmentStrategy {

    @Resource
    private CashBalanceAdjustmentStrategy cashBalanceStrategy;

    @Override
    public void adjustBalance(Account account, BigDecimal amount) {
        BigDecimal newBalance = account.getMealBalance().subtract(amount);
        try {
            account.applyMealBalance(newBalance);
        } catch (InsufficientBalanceException e) {
            cashBalanceStrategy.adjustBalance(account, amount);
        }
    }
}
