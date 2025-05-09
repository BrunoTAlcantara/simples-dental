package com.simplesdental.product.auth;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class SecurityConstants {

  public static final AntPathRequestMatcher[] PUBLIC_ENDPOINTS = {
      new AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name()),
      new AntPathRequestMatcher("/v3/api-docs/**"),
      new AntPathRequestMatcher("/swagger-ui/**"),
      new AntPathRequestMatcher("/swagger-ui.html"),
      new AntPathRequestMatcher("/swagger-resources/**"),
      new AntPathRequestMatcher("/webjars/**")
  };


  public static final AntPathRequestMatcher[] USER_ONLY_ENDPOINTS = {
      new AntPathRequestMatcher("/api/auth/password", HttpMethod.POST.name()),
      new AntPathRequestMatcher("/api/products/**", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/api/categories/**", HttpMethod.GET.name())
  };

  public static final AntPathRequestMatcher[] ADMIN_ENDPOINTS = {
      new AntPathRequestMatcher("/api/auth/register", HttpMethod.POST.name()),
      new AntPathRequestMatcher("/api/users/**"),
      new AntPathRequestMatcher("/api/products/**"),
      new AntPathRequestMatcher("/api/categories/**")
  };

}
