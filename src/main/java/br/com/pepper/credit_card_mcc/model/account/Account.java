package br.com.pepper.credit_card_mcc.model.account;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.exception.InsufficientBalanceException;

@Entity
@Table(name = "account")
public class Account implements Serializable {

    private static final long serialVersionUID = -2392080050407667213L;

    protected Account() {

    }

    public Account(Long id, BigDecimal foodBalance, BigDecimal mealBalance, BigDecimal cashBalance) {
        this.id = id;
        this.foodBalance = foodBalance;
        this.mealBalance = mealBalance;
        this.cashBalance = cashBalance;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Transient
    private MccType realMccTypeApplied;

    private BigDecimal foodBalance;
    private BigDecimal mealBalance;
    private BigDecimal cashBalance;

    public Long getId() {
        return id;
    }

    public BigDecimal getFoodBalance() {
        return foodBalance;
    }

    public BigDecimal getMealBalance() {
        return mealBalance;
    }

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public MccType getRealMccTypeApplied() {
        return realMccTypeApplied;
    }

    public void applyCashBalance(BigDecimal newBalance) {
        realMccTypeApplied = MccType.CASH;
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException(this, newBalance, MccType.CASH);
        }
        this.cashBalance = newBalance;
    }

    public void applyFoodBalance(BigDecimal newBalance) {
        realMccTypeApplied = MccType.FOOD;
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException(this, newBalance, MccType.FOOD);
        }
        this.foodBalance = newBalance;
    }

    public void applyMealBalance(BigDecimal newBalance) {
        realMccTypeApplied = MccType.MEAL;
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException(this, newBalance, MccType.MEAL);
        }
        this.mealBalance = newBalance;
    }
}
