package vn.core.authorization.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@EnableWebMvc
@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    log.info("(configure) enable http security");
    http
      .csrf()
      .disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .exceptionHandling().authenticationEntryPoint(
        (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
      .and()
      .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig),
        UsernamePasswordAuthenticationFilter.class)
      .authorizeRequests()
      .antMatchers(
        "/wallet/**",
              "/swagger-ui/**",
              "/v3/api-docs/**"
      )
      .permitAll()
      .anyRequest().authenticated();
  }

}
