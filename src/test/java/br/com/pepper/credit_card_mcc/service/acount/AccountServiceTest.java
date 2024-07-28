package br.com.pepper.credit_card_mcc.service.acount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import br.com.pepper.credit_card_mcc.exception.CustomException;
import br.com.pepper.credit_card_mcc.model.account.Account;
import br.com.pepper.credit_card_mcc.repository.account.AccountRepository;
import br.com.pepper.credit_card_mcc.service.account.AccountService;
import br.com.pepper.credit_card_mcc.utils.AbstractTestUtils;

class AccountServiceTest extends AbstractTestUtils {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void findAccountByIdWithValidation() {
        Long accountId = 1L;

        Account account = new Account(accountId, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Account result = accountService.findAccountByIdWithValidation(accountId);
        assertEquals(accountId, result.getId());
    }

    @Test
    void findAccountByIdWithValidationNotFound() {
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        CustomException thrown = assertThrows(CustomException.class, () ->
                accountService.findAccountByIdWithValidation(accountId)
        );
        assertEquals("Account not found", thrown.getMessage());
    }

    @Test
    void update() {
        Account account = new Account(1l, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);

        accountService.update(account);

        verify(accountRepository).save(account);
    }
}