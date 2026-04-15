package vn.core.authorization.config.security;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class JwtConfig {

  @Value("#{'${spring.security.jwt.uri:/auth/**}'.split(',')}")
  private List<String> uri;

  @Value("${spring.security.jwt.header:Authorization}")
  private String header;

  @Value("${spring.security.jwt.prefix:Bearer}")
  private String prefix;

  @Value("${spring.security.jwt.expiration:#{24*60*60}}")
  private int expiration;

  @Value("${spring.security.jwt.secret:AWR1cGlhTWF0aFNlY3JldEtleQ==}")
  private String secret;

  public List<String> getUri() {
    return uri;
  }

  public void setUri(List<String> uri) {
    this.uri = uri;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public int getExpiration() {
    return expiration;
  }

  public void setExpiration(int expiration) {
    this.expiration = expiration;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }
}
