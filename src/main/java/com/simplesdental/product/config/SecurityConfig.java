package com.simplesdental.product.config;


import static com.simplesdental.product.auth.SecurityConstants.ADMIN_ENDPOINTS;
import static com.simplesdental.product.auth.SecurityConstants.PUBLIC_ENDPOINTS;
import static com.simplesdental.product.auth.SecurityConstants.USER_ONLY_ENDPOINTS;

import com.simplesdental.product.auth.JwtAuthenticationEntryPoint;
import com.simplesdental.product.auth.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtFilter jwtFilter;
  private final UserDetailsService userDetailsService;
  private final JwtAuthenticationEntryPoint entryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sm ->
            sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .exceptionHandling(ex ->
            ex.authenticationEntryPoint(entryPoint)
        )
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll();
          auth.requestMatchers(USER_ONLY_ENDPOINTS).hasAnyRole("USER", "ADMIN");
          auth.requestMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN");
          auth.anyRequest().authenticated();
        })
        .userDetailsService(userDetailsService)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }
}