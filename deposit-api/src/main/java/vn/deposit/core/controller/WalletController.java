package vn.deposit.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import vn.deposit.core.model.request.DepositRequest;
import vn.deposit.core.service.WalletService;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

  @Value("${application.kafka.test-topic:test-topic}")
  private String testTopic;

  private final WalletService walletService;

  private final KafkaTemplate<String, String> kafkaTemplate;

  @PostMapping("/deposit")
  public String deposit(@RequestBody DepositRequest req) throws JsonProcessingException {
    walletService.deposit(req.getUserId(), req.getAmount(), req.getReferenceId());
    kafkaTemplate.send(testTopic, "message");
    return "OK";
  }

  @GetMapping("/balance/{userId}")
  public Long balance(@PathVariable String userId) {
    return walletService.getBalance(userId);
  }

  public static void main(String[] args) {
    System.out.println(System.currentTimeMillis());
  }

}
