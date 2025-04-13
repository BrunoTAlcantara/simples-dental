package com.simplesdental.product.enums;


import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
  ADMIN("admin"),
  USER("user");

  private final String value;
  
  public static Role fromString(String value) {
    return Arrays.stream(Role.values())
        .filter(role -> role.value.equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Role inválido: " + value));
  }
}

