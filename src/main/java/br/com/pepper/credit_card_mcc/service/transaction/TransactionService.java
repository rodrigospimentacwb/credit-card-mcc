package br.com.pepper.credit_card_mcc.service.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.enums.TransactionStatusType;
import br.com.pepper.credit_card_mcc.exception.InsufficientBalanceException;
import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.model.mechant.Merchant;
import br.com.pepper.credit_card_mcc.model.transaction.Transaction;
import br.com.pepper.credit_card_mcc.repository.transaction.TransactionRepository;
import br.com.pepper.credit_card_mcc.service.account.AccountService;
import br.com.pepper.credit_card_mcc.service.merchant.MerchantService;
import br.com.pepper.credit_card_mcc.service.transaction.strategy.BalanceAdjustmentContext;

@Service
public class TransactionService {

    Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Resource
    private BalanceAdjustmentContext balanceAdjustmentContext;

    @Resource
    private TransactionRepository transactionRepository;

    @Resource
    AccountService accountService;

    @Resource
    MerchantService merchantService;

    @Transactional
    public TransactionStatusType createTransaction(Account account, BigDecimal amount, String mcc, String merchant) {
        MccType mccType = MccType.fromMcc(mcc);
        mccType = tryGetMccByMerchantName(merchant, mccType);

        TransactionStatusType status = processBalanceAndReturnStatus(account, amount, mccType);
        Transaction transaction = persistTransaction(account, merchant, amount, status);
        return transaction.getStatus();
    }

    private MccType tryGetMccByMerchantName(String merchant, MccType mccType) {
        try {
            Merchant merchantFound = merchantService.findByNameIgnoreCase(merchant);
            mccType = merchantFound.getMcc();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return mccType;
    }

    private TransactionStatusType processBalanceAndReturnStatus(Account account, BigDecimal amount, MccType mccType) {
        TransactionStatusType status;
        try {
            balanceAdjustmentContext.adjustBalance(mccType, account, amount);
            status = TransactionStatusType.APPROVED;
        } catch (InsufficientBalanceException e) {
            status = TransactionStatusType.DECLINED;
        }
        return status;
    }

    private Transaction persistTransaction(Account account, String merchant, BigDecimal amount, TransactionStatusType status) {

        accountService.update(account);

        Transaction transaction = new Transaction.Builder()
                .account(account)
                .amount(amount)
                .mcc(account.getRealMccTypeApplied())
                .status(status)
                .merchantName(merchant)
                .createdOn(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);
        return transaction;
    }
}
