package vn.deposit.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReconciliationResult {
  List<BankTx> missing;
  List<BankTx> mismatch;
}
