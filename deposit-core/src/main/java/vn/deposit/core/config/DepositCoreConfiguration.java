package vn.deposit.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import vn.core.kafka.EnableCoreKafkaConsumer;
import vn.core.kafka.EnableCoreKafkaProducer;
import vn.core.authorization.config.EnableAuthenticationCore;
import vn.core.queue.config.EnableCoreQueueConsumer;
import vn.core.queue.config.EnableCoreQueuePublisher;

@Configuration
@ComponentScan(basePackages = {"vn.deposit.core.service"})
@EnableCoreKafkaConsumer
@EnableCoreKafkaProducer
@EnableCoreQueueConsumer
@EnableCoreQueuePublisher
@EnableAuthenticationCore
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"vn.deposit.core.repository"})
@EntityScan(basePackages = {"vn.deposit.core.entity"})
public class DepositCoreConfiguration {

}
