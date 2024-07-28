package br.com.pepper.credit_card_mcc.service.transaction.strategy;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.utils.AbstractTestUtils;

class BalanceAdjustmentContextTest extends AbstractTestUtils {

    private Account account;

    @Mock
    private CashBalanceAdjustmentStrategy cashBalanceAdjustmentStrategy;

    @Mock
    private FoodBalanceAdjustmentStrategy foodBalanceAdjustmentStrategy;

    @Mock
    private MealBalanceAdjustmentStrategy mealBalanceAdjustmentStrategy;

    @InjectMocks
    private BalanceAdjustmentContext balanceAdjustmentContext;

    @BeforeEach
    void setUp() {
        account = new Account(1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
    }

    @ParameterizedTest
    @CsvSource({ MCC_FOOD_CODE_1, MCC_FOOD_CODE_2, MCC_MEAL_CODE_1, MCC_MEAL_CODE_2, MCC_CASH_CODE, MCC_CASH_CODE })
    void adjustBalanceWithBalance(String mcc) {
        MccType mccType = MccType.fromMcc(mcc);
        BigDecimal amount = BigDecimal.ONE;

        balanceAdjustmentContext = new BalanceAdjustmentContext(cashBalanceAdjustmentStrategy, foodBalanceAdjustmentStrategy, mealBalanceAdjustmentStrategy);

        balanceAdjustmentContext.adjustBalance(mccType, account, amount);

        switch (mcc) {
            case MCC_FOOD_CODE_1, MCC_FOOD_CODE_2 ->
                    verify(foodBalanceAdjustmentStrategy).adjustBalance(account, amount);
            case MCC_MEAL_CODE_1, MCC_MEAL_CODE_2 ->
                    verify(mealBalanceAdjustmentStrategy).adjustBalance(account, amount);
            case MCC_CASH_CODE -> verify(cashBalanceAdjustmentStrategy).adjustBalance(account, amount);
            default -> throw new IllegalArgumentException("Unexpected MCC code: " + mcc);
        }
    }

    @ParameterizedTest
    @CsvSource({ MCC_FOOD_CODE_1, MCC_FOOD_CODE_2, MCC_MEAL_CODE_1, MCC_MEAL_CODE_2, MCC_CASH_CODE, MCC_CASH_CODE })
    void adjustBalanceWithoutBalance(String mcc) {
        MccType mccType = MccType.fromMcc(mcc);
        BigDecimal amount = new BigDecimal("11.24");

        balanceAdjustmentContext = new BalanceAdjustmentContext(cashBalanceAdjustmentStrategy, foodBalanceAdjustmentStrategy, mealBalanceAdjustmentStrategy);

        balanceAdjustmentContext.adjustBalance(mccType, account, amount);

        switch (mcc) {
            case MCC_FOOD_CODE_1, MCC_FOOD_CODE_2 ->
                    verify(foodBalanceAdjustmentStrategy).adjustBalance(account, amount);
            case MCC_MEAL_CODE_1, MCC_MEAL_CODE_2 ->
                    verify(mealBalanceAdjustmentStrategy).adjustBalance(account, amount);
            case MCC_CASH_CODE -> verify(cashBalanceAdjustmentStrategy).adjustBalance(account, amount);
            default -> throw new IllegalArgumentException("Unexpected MCC code: " + mcc);
        }
    }
}