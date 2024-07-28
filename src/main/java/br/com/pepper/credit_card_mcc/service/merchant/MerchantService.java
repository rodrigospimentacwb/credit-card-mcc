package br.com.pepper.credit_card_mcc.service.merchant;

import java.util.Optional;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.pepper.credit_card_mcc.exception.CustomException;
import br.com.pepper.credit_card_mcc.model.mechant.Merchant;
import br.com.pepper.credit_card_mcc.repository.merchant.MerchantRepository;

@Service
public class MerchantService {

    public Merchant findByNameIgnoreCase(String name) {
        Optional<Merchant> merchantOptional = repository.findByNameIgnoreCase(name);
        if (merchantOptional.isEmpty()) {
            throw new CustomException("Merchant not found with name: " + name);
        }
        return merchantOptional.get();
    }

    @Resource
    private MerchantRepository repository;
}
