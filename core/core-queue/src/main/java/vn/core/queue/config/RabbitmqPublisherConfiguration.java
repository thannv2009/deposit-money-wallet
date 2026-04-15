package vn.core.queue.config;

import vn.core.queue.constants.ConverterType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import vn.core.queue.service.QueueService;
import vn.core.queue.service.impl.QueueServiceRabbitMqCustomImpl;
import vn.core.queue.service.impl.QueueServiceRabbitMqImpl;

@Configuration
public class RabbitmqPublisherConfiguration {

  @Value("${application.queue.default_converter:Jackson2Json}")
  private ConverterType converterType;

  @Value("${application.queue.delay_time:60000}")
  private Integer delayTime;

  @Value("${application.queue.enable_custom:false}")
  private Boolean enableCustom;


  @Bean
  public QueueService queueServiceRabbitMQ(
    RabbitTemplate template, RabbitTemplate templateSimple
  ) {
    if (enableCustom) {
      return new QueueServiceRabbitMqCustomImpl(delayTime, template, templateSimple);
    } else {
      return new QueueServiceRabbitMqImpl(delayTime, template, templateSimple);
    }
  }

  @Bean(name = "rabbitTemplate")
  @Primary
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    if (converterType.equals(ConverterType.Jackson2Json)) {
      template.setMessageConverter(new Jackson2JsonMessageConverter());
    }
    if (converterType.equals(ConverterType.Serializer)) {
      template.setMessageConverter(new SerializerMessageConverter());
    }
    return template;
  }

  @Bean(name = "rabbitTemplateSimple")
  public RabbitTemplate rabbitTemplateSimple(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(new SimpleMessageConverter());
    return template;
  }

}
