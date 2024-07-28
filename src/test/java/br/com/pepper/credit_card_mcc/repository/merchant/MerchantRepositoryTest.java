package br.com.pepper.credit_card_mcc.repository.merchant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import jakarta.annotation.Resource;

import org.junit.jupiter.api.Test;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.model.mechant.Merchant;
import br.com.pepper.credit_card_mcc.utils.AbstractRepositoryTestUtils;

class MerchantRepositoryTest extends AbstractRepositoryTestUtils {

    @Resource
    private MerchantRepository repository;

    @Test
    void findByNameIgnoreCase() {
        Merchant merchant = new Merchant(1L, "Merchant Name", MccType.CASH);
        repository.save(merchant);

        Optional<Merchant> merchantFound = repository.findByNameIgnoreCase("Merchant Name");

        assertTrue(merchantFound.isPresent());
        assertEquals(1L, merchantFound.get().getId());
        assertEquals("Merchant Name", merchantFound.get().getName());
        assertEquals(MccType.CASH, merchantFound.get().getMcc());
    }

    @Test
    void findByNameIgnoreCaseNotFound() {
        Optional<Merchant> merchantFound = repository.findByNameIgnoreCase("Merchant Name");

        assertFalse(merchantFound.isPresent());
    }
}