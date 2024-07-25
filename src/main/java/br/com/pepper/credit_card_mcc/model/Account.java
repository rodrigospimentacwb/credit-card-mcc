package br.com.pepper.credit_card_mcc.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Account {

    public Account() {

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
}
