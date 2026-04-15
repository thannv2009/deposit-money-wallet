package vn.deposit.core.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

  @Value("${application.kafka.test-topic:test-topic}")
  private String testTopic;

  @Bean
  public NewTopic topicTest() {
    return new NewTopic(testTopic, 1, (short) 1);
  }

}
