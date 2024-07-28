package br.com.pepper.credit_card_mcc.repository.transaction;

import br.com.pepper.credit_card_mcc.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
