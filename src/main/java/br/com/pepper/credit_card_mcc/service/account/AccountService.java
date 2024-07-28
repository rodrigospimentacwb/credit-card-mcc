package br.com.pepper.credit_card_mcc.service.account;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pepper.credit_card_mcc.exception.CustomException;
import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.repository.account.AccountRepository;

@Service
public class AccountService {

    @Resource
    private AccountRepository repository;

    @Transactional(readOnly = true)
    public Account findAccountByIdWithValidation(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException("Account not found"));
    }

    @Transactional
    public Account update(Account account) {
        return repository.save(account);
    }
}
