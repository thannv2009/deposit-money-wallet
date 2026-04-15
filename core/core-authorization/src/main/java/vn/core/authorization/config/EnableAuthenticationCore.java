package vn.core.authorization.config;

import org.springframework.context.annotation.Import;
import vn.core.authorization.config.security.SecurityTokenConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import({
  AuthenticationCoreConfiguration.class, SecurityTokenConfig.class
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableAuthenticationCore {

}
