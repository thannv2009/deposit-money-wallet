package vn.deposit.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.deposit.core.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

  Transaction findByReferenceId(String referenceId);
}
