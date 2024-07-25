package br.com.pepper.credit_card_mcc.model;

import br.com.pepper.credit_card_mcc.enums.TransactionStatusEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {

    protected Transaction() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String merchantName;

    @Column(nullable = false, length = 4)
    private String mcc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private TransactionStatusEnum status;

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
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

    public String getMcc() {
        return mcc;
    }

    public TransactionStatusEnum getStatus() {
        return status;
    }

    private Transaction(Builder builder) {
        this.id = builder.id;
        this.accountId = builder.accountId;
        this.createdOn = builder.createdOn;
        this.amount = builder.amount;
        this.merchantName = builder.merchantName;
        this.mcc = builder.mcc;
        this.status = builder.status;
    }

    public static class Builder {
        private Long id;
        private Long accountId;
        private LocalDateTime createdOn;
        private BigDecimal amount;
        private String merchantName;
        private String mcc;
        private TransactionStatusEnum status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder accountId(Long accountId) {
            this.accountId = accountId;
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

        public Builder mcc(String mcc) {
            this.mcc = mcc;
            return this;
        }

        public Builder status(TransactionStatusEnum status) {
            this.status = status;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}

