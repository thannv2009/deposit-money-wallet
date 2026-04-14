package vn.deposit.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.deposit.core.entity.Transaction;
import vn.deposit.core.model.BankTx;
import vn.deposit.core.model.ReconciliationResult;
import vn.deposit.core.service.ReconciliationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReconciliationServiceImpl implements ReconciliationService {

  @Override
  public ReconciliationResult reconcile(
    List<BankTx> bank,
    List<Transaction> db) {

    Map<String, Transaction> dbMap =
      db.stream()
        .collect(Collectors.toMap(
          Transaction::getReferenceId,
          t -> t));

    List<BankTx> missing = new ArrayList<>();
    List<BankTx> mismatch = new ArrayList<>();

    for (BankTx b : bank) {

      Transaction local = dbMap.get(b.getRef());

      if (local == null)
        missing.add(b);

      else if (local.getAmount()
        .compareTo(b.getAmount()) != 0)
        mismatch.add(b);
    }

    return new ReconciliationResult(missing, mismatch);
  }

}
