package vn.deposit.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.deposit.core.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
   Wallet findByUserId(String userId);
}
