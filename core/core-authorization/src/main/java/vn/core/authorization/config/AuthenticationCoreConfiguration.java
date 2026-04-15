package vn.core.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.core.authorization.config.security.JwtConfig;
import vn.core.authorization.config.security.JwtTokenAuthenticationFilter;

@Configuration
public class AuthenticationCoreConfiguration {

  @Bean
  public JwtConfig getJwtConfig() {
    return new JwtConfig();
  }

  @Bean
  public JwtTokenAuthenticationFilter getJwtTokenAuthenticationFilter(
    JwtConfig jwtConfig
  ) {
    return new JwtTokenAuthenticationFilter(
      jwtConfig);
  }

}
