package vn.core.authorization.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                    .addSecuritySchemes("bearerAuth",
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")));
  }

}
