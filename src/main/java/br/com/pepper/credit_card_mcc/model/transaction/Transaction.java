package br.com.pepper.credit_card_mcc.model.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.enums.TransactionStatusType;
import br.com.pepper.credit_card_mcc.model.account.Account;

@Entity
@Table(name = "transaction")
public class Transaction {

    protected Transaction() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String merchantName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 4)
    private MccType mcc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private TransactionStatusType status;

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public MccType getMcc() {
        return mcc;
    }

    public TransactionStatusType getStatus() {
        return status;
    }

    private Transaction(Builder builder) {
        this.id = builder.id;
        this.account = builder.account;
        this.createdOn = builder.createdOn;
        this.amount = builder.amount;
        this.merchantName = builder.merchantName;
        this.mcc = builder.mcc;
        this.status = builder.status;
    }

    public static class Builder {
        private Long id;
        private Account account;
        private LocalDateTime createdOn;
        private BigDecimal amount;
        private String merchantName;
        private MccType mcc;
        private TransactionStatusType status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder account(Account account) {
            this.account = account;
            return this;
        }

        public Builder createdOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder merchantName(String merchantName) {
            this.merchantName = merchantName;
            return this;
        }

        public Builder mcc(MccType mcc) {
            this.mcc = mcc;
            return this;
        }

        public Builder status(TransactionStatusType status) {
            this.status = status;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}

