package vn.deposit.core.service;

import vn.deposit.core.entity.Transaction;
import vn.deposit.core.model.BankTx;
import vn.deposit.core.model.ReconciliationResult;

import java.util.List;

public interface ReconciliationService {

  ReconciliationResult reconcile(
    List<BankTx> bank,
    List<Transaction> db
  );

}
