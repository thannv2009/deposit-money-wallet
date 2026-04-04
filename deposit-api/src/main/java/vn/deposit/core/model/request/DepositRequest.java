package vn.deposit.core.model.request;

import lombok.Data;

@Data
public class DepositRequest {
  private String userId;
  private Long amount;
  private String referenceId;
}
