package vn.core.authorization;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfiguration {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Bean
  public Map<String, Object> producerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

    return props;
  }

  @Bean
  @Primary
  public KafkaTemplate<String, Object> kafkaTemplate() {
    ProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(
      producerConfigs()
    );

    return new KafkaTemplate<>(factory);
  }

  @Bean
  public Map<String, Object> producerConfigsStringSerializer() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

    return props;
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplateStringSerializer() {
    ProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(
      producerConfigsStringSerializer()
    );

    return new KafkaTemplate<>(factory);
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplateTransaction() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "producer-transaction");

    ProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(
      props
    );

    return new KafkaTemplate<>(factory);
  }
}
