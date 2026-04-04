package vn.deposit.core.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="wallets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet extends BaseEntity {

  private String userId;
  private Long balance = 0L;
  public void deposit(Long amount) { this.balance += amount; }

}
