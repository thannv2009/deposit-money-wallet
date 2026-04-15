package vn.core.queue.config;

import vn.core.queue.constants.ConverterType;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConsumerConfiguration {

  @Value("${application.queue.default_converter:Jackson2Json}")
  private ConverterType converterType;

  @Value("${application.queue.concurrent_consumers:5}")
  private int concurrentConsumers;

  @Value("${application.queue.max_concurrent_consumers:30}")
  private int maxConcurrentConsumers;

  @Value("${application.queue.prefetch:1}")
  private int prefetch;

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
    ConnectionFactory connectionFactory,
    ApplicationQueueRetryProperties queueRetryProperties
  ) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConcurrentConsumers(concurrentConsumers);
    factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
    factory.setPrefetchCount(prefetch);
    factory.setConnectionFactory(connectionFactory);
//    factory.setObservationEnabled(true);

    factory.setAdviceChain(
      RetryInterceptorBuilder.stateless()
        .maxAttempts(queueRetryProperties.getMaxAttempts())
        .backOffOptions(
          queueRetryProperties.getInitialInterval().toMillis(),
          queueRetryProperties.getMultiplier(),
          queueRetryProperties.getMaxInterval().toMillis()
        ).build()
    );

    if (converterType.equals(ConverterType.Jackson2Json)) {
      factory.setMessageConverter(new Jackson2JsonMessageConverter());
    }
    if (converterType.equals(ConverterType.Serializer)) {
      factory.setMessageConverter(new SerializerMessageConverter());
    }

    return factory;
  }

}
