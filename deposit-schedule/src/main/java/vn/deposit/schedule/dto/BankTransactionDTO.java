package vn.deposit.schedule.dto;

import lombok.Data;

@Data
public class BankTransactionDTO {
    private String bankTrxId;
    private Double amount;
    private String status;
    private String trxTime;
}
