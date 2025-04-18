package com.simplesdental.product.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserContext {

  public static UserDetailsImpl getAuthenticatedUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) {
      return null;
    }

    Object principal = auth.getPrincipal();
    return (principal instanceof UserDetailsImpl user) ? user : null;
  }

  public static Long getUserId() {
    UserDetailsImpl user = getAuthenticatedUser();
    return user != null ? user.getId() : null;
  }

  public static String getUserEmail() {
    UserDetailsImpl user = getAuthenticatedUser();
    return user != null ? user.getEmail() : null;
  }

  public static String getUserRole() {
    UserDetailsImpl user = getAuthenticatedUser();
    return user != null ? user.getRole() : null;
  }
}
