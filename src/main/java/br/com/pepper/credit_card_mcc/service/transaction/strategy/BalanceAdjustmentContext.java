package br.com.pepper.credit_card_mcc.service.transaction.strategy;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.model.account.Account;

@Service
public class BalanceAdjustmentContext {

    private final Map<MccType, BalanceAdjustmentStrategy> strategyMap = new EnumMap<>(MccType.class);

    public BalanceAdjustmentContext(CashBalanceAdjustmentStrategy cashStrategy, FoodBalanceAdjustmentStrategy foodStrategy, MealBalanceAdjustmentStrategy mealStrategy) {
        strategyMap.put(MccType.CASH, cashStrategy);
        strategyMap.put(MccType.FOOD, foodStrategy);
        strategyMap.put(MccType.MEAL, mealStrategy);
    }

    public void adjustBalance(MccType mccType, Account account, BigDecimal amount) {
        BalanceAdjustmentStrategy strategy = strategyMap.getOrDefault(mccType, new CashBalanceAdjustmentStrategy());
        strategy.adjustBalance(account, amount);
    }
}
