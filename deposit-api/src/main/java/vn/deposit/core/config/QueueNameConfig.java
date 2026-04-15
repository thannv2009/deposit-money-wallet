package vn.deposit.core.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueNameConfig {

  @Value("${application.queue.test:queueTest}")
  private String queueTest;

  @Bean
  public Queue queueTest() {
    return new Queue(queueTest);
  }

}
