package com.simplesdental.product.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesdental.product.exception.errors.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      org.springframework.security.core.AuthenticationException authException)
      throws IOException {

    ApiError apiError = ApiError.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.UNAUTHORIZED.value())
        .error("Não autenticado")
        .message("Você precisa estar autenticado para acessar este recurso.")
        .path(request.getRequestURI())
        .build();

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");

    objectMapper.writeValue(response.getOutputStream(), apiError);
  }
}
