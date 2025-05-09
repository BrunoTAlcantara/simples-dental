package com.simplesdental.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.simplesdental.product.dto.UserRequestDTO;
import com.simplesdental.product.dto.UserResponseDTO;
import com.simplesdental.product.enums.Role;
import com.simplesdental.product.exception.custom.ModelNotFoundException;
import com.simplesdental.product.mapper.UserMapper;
import com.simplesdental.product.model.User;
import com.simplesdental.product.repository.UserRepository;
import com.simplesdental.product.shared.ResponsePageModel;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  private User user;
  private UserRequestDTO requestDTO;
  private UserResponseDTO responseDTO;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setName("Bruno");
    user.setEmail("bruno@teste.com");
    user.setPassword("encodedSenha");
    user.setRole(Role.ADMIN);

    requestDTO = new UserRequestDTO("Bruno", "bruno@teste.com", "senha", Role.ADMIN);
    responseDTO = new UserResponseDTO(1L, "Bruno", "bruno@teste.com", Role.ADMIN);
  }

  @Test
  void shouldUpdateUserSuccessfully() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(passwordEncoder.encode("senha")).thenReturn("encodedSenha");
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

    UserResponseDTO result = userService.update(1L, requestDTO);

    assertThat(result.id()).isEqualTo(1L);
    assertThat(result.name()).isEqualTo("Bruno");
    verify(userRepository).save(user);
  }

  @Test
  void shouldThrowWhenUserNotFoundOnUpdate() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.update(99L, requestDTO))
        .isInstanceOf(ModelNotFoundException.class)
        .hasMessageContaining("Usuário 99 não encontrado");
  }

  @Test
  void shouldDeleteUserSuccessfully() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    doNothing().when(userRepository).delete(user);

    userService.delete(1L);

    verify(userRepository).delete(user);
  }

  @Test
  void shouldThrowWhenUserNotFoundOnDelete() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.delete(99L))
        .isInstanceOf(ModelNotFoundException.class)
        .hasMessageContaining("Usuário 99 não encontrado");
  }

  @Test
  void shouldFindUserById() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

    UserResponseDTO result = userService.findById(1L);

    assertThat(result.id()).isEqualTo(1L);
    assertThat(result.email()).isEqualTo("bruno@teste.com");
  }

  @Test
  void shouldThrowWhenUserNotFoundById() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.findById(99L))
        .isInstanceOf(ModelNotFoundException.class)
        .hasMessageContaining("Usuário 99 não encontrado");
  }

  @Test
  void shouldReturnPagedUsers() {
    Page<User> page = new PageImpl<>(List.of(user));
    when(userRepository.findAll(any(PageRequest.class))).thenReturn(page);
    when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

    ResponsePageModel<UserResponseDTO> result = userService.findAll(PageRequest.of(0, 10));

    assertThat(result.totalItems()).isEqualTo(1);
    assertThat(result.items().get(0).email()).isEqualTo("bruno@teste.com");
  }
}
