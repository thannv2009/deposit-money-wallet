package vn.deposit.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankTx {
  private String ref;
  private Long amount;
}
