package atd.demo.keycloakdemo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final KeycloakLogoutHandler keycloakLogoutHandler;

  @Bean
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  //  filter intercepts requests and applies the needed logic for OAuth 2 authentication
  @Order(1)
  @Bean
  public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
        .requestMatchers(new AntPathRequestMatcher("/"))
        .permitAll()
        .anyRequest()
        .authenticated();
    // enable OAuth2 login
    http.oauth2Login().and().logout().addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/");
    return http.build();
  }

  @Order(2)
  @Bean
  public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
    // request to /customers/* will only be authorized if the one requesting it is an authenticated
    // user with the role USER
    http.authorizeHttpRequests()
        .requestMatchers(new AntPathRequestMatcher("/customers*"))
        .hasRole("USER")
        .anyRequest()
        .authenticated();

    // validate the bound JWT token against our Keycloak server
    http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class).build();
  }
}
