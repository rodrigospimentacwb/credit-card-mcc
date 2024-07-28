package br.com.pepper.credit_card_mcc.exception;

import java.math.BigDecimal;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.model.account.Account;

public class InsufficientBalanceException extends RuntimeException {

    private final Account account;
    private final BigDecimal requestedAmount;
    private final MccType mccType;

    public InsufficientBalanceException(Account account, BigDecimal requestedAmount, MccType mccType) {
        super(String.format("Insufficient balance in account '%s' for the requested amount of %s to MCC %s.",
                account.getId(), requestedAmount.toString(), mccType.toString()));
        this.account = account;
        this.requestedAmount = requestedAmount;
        this.mccType = mccType;
    }

    public InsufficientBalanceException(String message, Account account, BigDecimal requestedAmount, MccType mccType) {
        super(message);
        this.account = account;
        this.requestedAmount = requestedAmount;
        this.mccType = mccType;
    }

    public InsufficientBalanceException(String message, Throwable cause, Account account, BigDecimal requestedAmount, MccType mccType) {
        super(message, cause);
        this.account = account;
        this.requestedAmount = requestedAmount;
        this.mccType = mccType;
    }

    public Account getAccount() {
        return account;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public MccType getMccType() {
        return mccType;
    }
}
