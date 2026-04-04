package vn.deposit.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.deposit.core.contants.Status;
import vn.deposit.core.contants.TransactionType;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name="transactions", uniqueConstraints = @UniqueConstraint(columnNames = "referenceId"))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseEntity {

  private String walletId;
  private Long amount;
  private TransactionType type;
  private Status status;
  private String referenceId;


}
