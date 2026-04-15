package vn.core.queue.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import vn.core.queue.service.QueueService;

import java.util.List;


@Slf4j
public class QueueServiceRabbitMqCustomImpl extends QueueServiceRabbitMqImpl implements
  QueueService {

  @Value("${application.queue-service.enable_retry:false}")
  private Boolean enableRetry;

  public QueueServiceRabbitMqCustomImpl(int delayTime, RabbitTemplate template,
                                        RabbitTemplate templateSimple) {
    super(delayTime, template, templateSimple);
    log.info("(QueueCustomService)init");
  }


  @Override
  public void push(String message, String queue) {
    log.info("(push)message: {}, queue: {}, enableRetry: {}", message, queue, enableRetry);
    if (!enableRetry && queue.contains("delay")) {
      //do nothing
      log.info("(push) --> NOT_PUSH_DELAY_QUEUE");
      return;
    }
    super.push(message, queue);
  }

  @Override
  public void push(Object message, String queue) {
    log.info("(push)message: {}, queue: {}, enableRetry: {}", message, queue, enableRetry);
    if (!enableRetry && queue.contains("delay")) {
      //do nothing
      log.info("(push) --> NOT_PUSH_DELAY_QUEUE");
      return;
    }
    super.push(message, queue);
  }

  /**
   * Push messages to queue.
   *
   * @param messages Messages
   * @param queue    Queue
   */
  @Override
  public void push(List<Object> messages, String queue) {
    log.info(
      "(push)queue: {}, messages: {}, enableRetry: {}", queue, messages.size(), enableRetry
    );
    if (!enableRetry && queue.contains("delay")) {
      //do nothing
      log.info("(push) --> NOT_PUSH_DELAY_QUEUE");
      return;
    }
    super.push(messages, queue);
  }


}
