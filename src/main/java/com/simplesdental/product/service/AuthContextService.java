package com.simplesdental.product.service;

import com.simplesdental.product.dto.ContextResponseDTO;
import com.simplesdental.product.exception.custom.ModelNotFoundException;
import com.simplesdental.product.model.User;
import com.simplesdental.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthContextService {

  private final UserRepository userRepository;

  @Cacheable(value = "userContext", key = "#email")
  public ContextResponseDTO getContext(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ModelNotFoundException("Usuário não encontrado", null));

    return new ContextResponseDTO(user.getId(), user.getEmail(), user.getRole().name());
  }
}
