package com.simplesdental.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.simplesdental.product.dto.ContextResponseDTO;
import com.simplesdental.product.enums.Role;
import com.simplesdental.product.exception.custom.ModelNotFoundException;
import com.simplesdental.product.model.User;
import com.simplesdental.product.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthContextServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private AuthContextService authContextService;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setEmail("bruno@teste.com");
    user.setRole(Role.ADMIN);
  }

  @Test
  void shouldReturnContextWhenUserExists() {
    when(userRepository.findByEmail("bruno@teste.com")).thenReturn(Optional.of(user));

    ContextResponseDTO result = authContextService.getContext("bruno@teste.com");

    assertThat(result.id()).isEqualTo(1L);
    assertThat(result.email()).isEqualTo("bruno@teste.com");
    assertThat(result.role()).isEqualTo("ADMIN");
  }

  @Test
  void shouldThrowWhenUserNotFound() {
    when(userRepository.findByEmail("naoexiste@teste.com")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authContextService.getContext("naoexiste@teste.com"))
        .isInstanceOf(ModelNotFoundException.class)
        .hasMessageContaining("Usuário não encontrado");
  }
}
