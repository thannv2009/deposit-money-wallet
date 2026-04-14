package vn.deposit.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import vn.deposit.core.entity.Transaction;
import vn.deposit.core.entity.Wallet;
import vn.deposit.core.model.BankTx;
import vn.deposit.core.model.ReconciliationResult;
import vn.deposit.core.repository.TransactionRepository;
import vn.deposit.core.repository.WalletRepository;
import vn.deposit.core.service.ReconciliationService;
import vn.deposit.core.service.WalletService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class DepositTest {

  @Mock
  private WalletRepository walletRepository;

  @InjectMocks
  private WalletService walletService;


  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private ReconciliationService service;

  public static void main(String[] args) {
    System.out.println(System.currentTimeMillis());
  }


  @Test
  void testDeposit_success() throws JsonProcessingException {
    String userId = "1";
    Long depositAmount = 100L;
    String referenceId = "TXN123";

    Wallet wallet = new Wallet();
    wallet.setId("W1");
    wallet.setUserId(userId);
    wallet.setBalance(500L);

    Mockito.when(walletRepository.findByUserId(userId)).thenReturn(wallet);
    Mockito.when(walletRepository.save(Mockito.any(Wallet.class))).thenAnswer(i -> i.getArgument(0));
    Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

    Wallet updatedWallet = walletService.deposit(userId, depositAmount, referenceId);

    Assertions.assertEquals(600L, Optional.ofNullable(updatedWallet.getBalance()));
    Mockito.verify(walletRepository).save(wallet);
    Mockito.verify(transactionRepository).save(Mockito.any(Transaction.class));
  }

  @Test
  void testDeposit_walletNotFound() {
    String userId = "2";
    Long depositAmount = 50L;
    String referenceId = "TXN999";

    Mockito.when(walletRepository.findByUserId(userId)).thenReturn(null);

    RuntimeException ex = assertThrows(RuntimeException.class, () ->
      walletService.deposit(userId, depositAmount, referenceId)
    );

    Assertions.assertEquals("Wallet not found", ex.getMessage());
  }

  @Test
  void testReconcile_missingAndMismatch() {
    // DB transactions
    Transaction t1 = new Transaction();
    t1.setReferenceId("TXN1");
    t1.setAmount(100L);

    Transaction t2 = new Transaction();
    t2.setReferenceId("TXN2");
    t2.setAmount(200L);

    List<Transaction> db = Arrays.asList(t1, t2);

    // Bank transactions
    BankTx b1 = new BankTx("TXN1", 100L); // match
    BankTx b2 = new BankTx("TXN2", 250L); // mismatch
    BankTx b3 = new BankTx("TXN3", 50L);  // missing

    List<BankTx> bank = Arrays.asList(b1, b2, b3);

    ReconciliationResult result = service.reconcile(bank, db);

    Assertions.assertEquals(1, result.getMissing().size());
    Assertions.assertEquals("TXN3", result.getMissing().get(0).getRef());

    Assertions.assertEquals(1, result.getMismatch().size());
    Assertions.assertEquals("TXN2", result.getMismatch().get(0).getRef());
  }

  @Test
  void testReconcile_allMatch() {
    Transaction t1 = new Transaction();
    t1.setReferenceId("TXN1");
    t1.setAmount(100L);

    List<Transaction> db = List.of(t1);
    List<BankTx> bank = List.of(new BankTx("TXN1", 100L));

    ReconciliationResult result = service.reconcile(bank, db);

    Assertions.assertEquals(0, result.getMissing().size());
    Assertions.assertEquals(0, result.getMismatch().size());
  }

  @Test
  void testReconcile_allMissing() {
    List<Transaction> db = List.of(); // DB empty
    List<BankTx> bank = List.of(new BankTx("TXN1", 100L));

    ReconciliationResult result = service.reconcile(bank, db);

    Assertions.assertEquals(1, result.getMissing().size());
    Assertions.assertEquals("TXN1", result.getMissing().get(0).getRef());
    Assertions.assertEquals(0, result.getMismatch().size());
  }

}
