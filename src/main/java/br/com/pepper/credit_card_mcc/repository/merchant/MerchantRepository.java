package br.com.pepper.credit_card_mcc.repository.merchant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.pepper.credit_card_mcc.model.mechant.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @Query("SELECT m FROM Merchant m WHERE UPPER(m.name) = UPPER(:name)")
    Optional<Merchant> findByNameIgnoreCase(@Param("name") String name);
}
