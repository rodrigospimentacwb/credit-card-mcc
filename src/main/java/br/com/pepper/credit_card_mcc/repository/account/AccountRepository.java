package br.com.pepper.credit_card_mcc.repository.account;

import br.com.pepper.credit_card_mcc.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
