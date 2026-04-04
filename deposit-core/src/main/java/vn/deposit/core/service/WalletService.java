package vn.deposit.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import vn.deposit.core.entity.Wallet;

public interface WalletService {

  Wallet deposit(String userId, Long amount, String refId) throws JsonProcessingException;

  Long getBalance(String userId);

}
