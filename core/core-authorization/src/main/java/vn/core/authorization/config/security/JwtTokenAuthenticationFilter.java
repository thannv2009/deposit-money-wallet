package vn.core.authorization.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

  private final JwtConfig jwtConfig;

  public JwtTokenAuthenticationFilter(
    JwtConfig jwtConfig
  ) {
    this.jwtConfig = jwtConfig;
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain
  ) throws ServletException, IOException {
    String header = request.getHeader(jwtConfig.getHeader());
    if (header == null || header.isEmpty()) {
      header = request.getHeader("Authentication");
    }
    String url = request.getRequestURI();
    log.info("(doFilterInternal) request url: {}: {}", request.getMethod(), url);
    if (header == null || header.isEmpty()) {
      chain.doFilter(request, response);
      return;
    }
    String tokens = header.replace(jwtConfig.getPrefix(), "").trim();
    String username = "anonymousUser";
    try {

      Claims claims = Jwts.parser()
        .setSigningKey(jwtConfig.getSecret().getBytes())
        .parseClaimsJws(tokens)
        .getBody();
      if (claims.getSubject() != null) {
        username = claims.getSubject();
      }

      if (username != null) {
        List<String> authorities = new ArrayList<>();
        if (claims.get("authorities") != null) {
          authorities = (List<String>) claims.get("authorities");
        }
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
          username, tokens,
          authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

        SecurityContextHolder.getContext().setAuthentication(auth);
      }

    } catch (Exception e) {
      SecurityContextHolder.clearContext();
    }
    chain.doFilter(request, response);
  }
}
