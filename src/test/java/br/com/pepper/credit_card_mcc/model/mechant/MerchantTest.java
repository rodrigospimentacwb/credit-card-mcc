package br.com.pepper.credit_card_mcc.model.mechant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import br.com.pepper.credit_card_mcc.enums.MccType;

class MerchantTest {

    @Test
    void merchantCreation() {
        String name = "Test Merchant";
        MccType mcc = MccType.FOOD;
        LocalDateTime before = LocalDateTime.now();

        Merchant merchant = new Merchant(1L, name, mcc);

        assertEquals(name, merchant.getName());
        assertEquals(mcc, merchant.getMcc());
        assertNotNull(merchant.getCreatedOn());
        assertNotNull(merchant.getId());
        LocalDateTime createdOn = merchant.getCreatedOn();
        assertTrue(createdOn.isAfter(before) || createdOn.isEqual(before));
    }

    @Test
    void merchantDefaultConstructor() {
        Merchant merchant = new Merchant();

        assertNull(merchant.getId());
        assertNull(merchant.getCreatedOn());
        assertNull(merchant.getName());
        assertNull(merchant.getMcc());
    }
}
