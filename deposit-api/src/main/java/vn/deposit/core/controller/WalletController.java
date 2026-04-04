package vn.deposit.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.deposit.core.model.request.DepositRequest;
import vn.deposit.core.service.WalletService;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
  private final WalletService walletService;

  @PostMapping("/deposit")
  public String deposit(@RequestBody DepositRequest req) throws JsonProcessingException {
    walletService.deposit(req.getUserId(), req.getAmount(), req.getReferenceId());
    return "OK";
  }

  @GetMapping("/balance/{userId}")
  public Long balance(@PathVariable String userId) {
    return walletService.getBalance(userId);
  }

}
