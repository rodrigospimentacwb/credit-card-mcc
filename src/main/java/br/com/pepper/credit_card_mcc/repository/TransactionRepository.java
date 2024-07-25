package br.com.pepper.credit_card_mcc.repository;

import br.com.pepper.credit_card_mcc.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
