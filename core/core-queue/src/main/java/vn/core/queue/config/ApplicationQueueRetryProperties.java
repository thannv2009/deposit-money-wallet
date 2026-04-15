package vn.core.queue.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@ConfigurationProperties(prefix = "application.queue.retry")
@Component
@Data
@NoArgsConstructor
public class ApplicationQueueRetryProperties {

  /**
   * Maximum number of attempts to deliver a message.
   */
  private int maxAttempts = 2;

  /**
   * Duration between the first and second attempt to deliver a message.
   */
  private Duration initialInterval = Duration.ofMillis(30000);

  /**
   * Multiplier to apply to the previous retry interval.
   */
  private double multiplier = 2.0;

  /**
   * Maximum duration between attempts.
   */
  private Duration maxInterval = Duration.ofMillis(60000);

}
