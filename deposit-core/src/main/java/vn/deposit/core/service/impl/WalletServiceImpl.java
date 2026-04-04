package vn.deposit.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.deposit.core.contants.Status;
import vn.deposit.core.contants.TransactionType;
import vn.deposit.core.entity.Transaction;
import vn.deposit.core.entity.Wallet;
import vn.deposit.core.repository.TransactionRepository;
import vn.deposit.core.repository.WalletRepository;
import vn.deposit.core.service.WalletService;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
  private final WalletRepository walletRepo;
  private final TransactionRepository txRepo;

  @Override
  @Transactional
  public Wallet deposit(String userId, Long amount, String refId) throws JsonProcessingException {
    if (txRepo.findByReferenceId(refId) != null) return null;

    Wallet wallet = walletRepo.findByUserId(userId);
    if (wallet == null) {
      wallet = walletRepo.save(new Wallet( userId, 0L));
    }

    wallet.deposit(amount);
    walletRepo.save(wallet);

    Transaction tx = new Transaction(
      wallet.getId(),
      amount,
      TransactionType.DEPOSIT,
      Status.SUCCESS,
      refId
    );

    txRepo.save(tx);
    return wallet;

  }

  @Override
  public Long getBalance(String userId) {
    return walletRepo.findById(userId)
      .map(Wallet::getBalance)
      .orElse(0L);
  }

}
