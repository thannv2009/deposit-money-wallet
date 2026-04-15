package vn.deposit.core.listenner;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TestKafkaListener {

  @KafkaListener(
    topics = "${application.kafka.test-topic:test-topic}",
    groupId = "send-topic-test",
    containerFactory = "kafkaListenerContainerFactory"
  )
  public void testKafkaListener(String message) {

    System.out.println("TestKafkaListener" + message);

  }

  @RabbitListener(queues = "${application.queue.test:queueTest}")
  public void trackingEventUserListener(String message) {
    System.out.println("TestRabbitMqListener" + message);
  }

}
