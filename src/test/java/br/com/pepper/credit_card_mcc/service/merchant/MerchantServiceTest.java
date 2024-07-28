package br.com.pepper.credit_card_mcc.service.merchant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import br.com.pepper.credit_card_mcc.enums.MccType;
import br.com.pepper.credit_card_mcc.exception.CustomException;
import br.com.pepper.credit_card_mcc.model.mechant.Merchant;
import br.com.pepper.credit_card_mcc.repository.merchant.MerchantRepository;
import br.com.pepper.credit_card_mcc.utils.AbstractTestUtils;

class MerchantServiceTest extends AbstractTestUtils {

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantService merchantService;

    @Test
    void findByNameIgnoreCaseFound() {
        String name = "Test Merchant";
        Merchant merchant = new Merchant(1L, "John Doo", MccType.FOOD);
        when(merchantRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(merchant));

        Merchant foundMerchant = merchantService.findByNameIgnoreCase(name);

        assertEquals(1L, foundMerchant.getId());
        assertEquals("John Doo", foundMerchant.getName());
        assertEquals(MccType.FOOD, foundMerchant.getMcc());
    }

    @Test
    void findByNameIgnoreCaseNotFound() {
        String name = "Nonexistent Merchant";
        when(merchantRepository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomException.class, () -> {
            merchantService.findByNameIgnoreCase(name);
        });
        assertEquals("Merchant not found with name: Nonexistent Merchant", exception.getMessage());
    }
}
