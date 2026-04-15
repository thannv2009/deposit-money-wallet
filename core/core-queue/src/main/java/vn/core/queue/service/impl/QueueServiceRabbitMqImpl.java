package vn.core.queue.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import vn.core.queue.service.QueueService;

import java.util.List;

@Slf4j
public class QueueServiceRabbitMqImpl implements QueueService {

  private final int delayTime;
  private final RabbitTemplate template;
  private final RabbitTemplate templateSimple;

  public QueueServiceRabbitMqImpl(
    int delayTime, RabbitTemplate template, RabbitTemplate templateSimple
  ) {
    this.delayTime = delayTime;
    this.template = template;
    this.templateSimple = templateSimple;
  }


  @Override
  public void push(String message, String queue) {
    log.info("(push)message: {}, queue: {}", message, queue);
    templateSimple.convertAndSend(queue, message);
  }

  @Override
  public void push(Object message, String queue) {
    log.info("(push)message: {}, queue: {}", message, queue);
    template.convertAndSend(queue, message);
  }

  /**
   * Push messages to queue.
   *
   * @param messages Messages
   * @param queue    Queue
   */
  @Override
  public void push(List<Object> messages, String queue) {
    log.info("(push)queue: {}, messages: {}", queue, messages.size());
    for (Object message : messages) {
      template.convertAndSend(queue, message);
    }
  }

}
