package com.simplesdental.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.simplesdental.product.auth.JwtUtil;
import com.simplesdental.product.dto.AuthRequestDTO;
import com.simplesdental.product.dto.AuthResponseDTO;
import com.simplesdental.product.dto.UpdatePasswordDTO;
import com.simplesdental.product.dto.UserRequestDTO;
import com.simplesdental.product.enums.Role;
import com.simplesdental.product.exception.custom.BusinessException;
import com.simplesdental.product.exception.custom.ModelNotFoundException;
import com.simplesdental.product.mapper.UserMapper;
import com.simplesdental.product.model.User;
import com.simplesdental.product.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private AuthenticationManager authenticationManager;
  @Mock
  private JwtUtil jwtUtil;
  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private AuthService authService;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setEmail("user@teste.com");
    user.setPassword("encodedPassword");
    user.setRole(Role.USER);
  }

  @Test
  void shouldLoginSuccessfully() {
    AuthRequestDTO request = new AuthRequestDTO("user@teste.com", "123456");
    UserDetails userDetails = mock(UserDetails.class);
    Authentication authentication = mock(Authentication.class);

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(jwtUtil.generateToken(userDetails)).thenReturn("fake-jwt-token");

    AuthResponseDTO response = authService.login(request);

    assertThat(response.token()).isEqualTo("fake-jwt-token");
  }

  @Test
  void shouldUpdateOwnPasswordSuccessfully() {
    UpdatePasswordDTO dto = new UpdatePasswordDTO("oldPass", "newPass");
    SecurityContextHolder.setContext(new SecurityContextImpl(
        new UsernamePasswordAuthenticationToken(user.getEmail(), null)));

    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("oldPass", user.getPassword())).thenReturn(true);
    when(passwordEncoder.encode("newPass")).thenReturn("hashedNewPass");
    when(userRepository.save(user)).thenReturn(user);

    authService.updateOwnPassword(dto);

    verify(userRepository).save(user);
    assertThat(user.getPassword()).isEqualTo("hashedNewPass");
  }

  @Test
  void shouldThrowWhenUserNotFoundInUpdatePassword() {
    UpdatePasswordDTO dto = new UpdatePasswordDTO("oldPass", "newPass");
    SecurityContextHolder.setContext(new SecurityContextImpl(
        new UsernamePasswordAuthenticationToken("notfound@teste.com", null)));

    when(userRepository.findByEmail("notfound@teste.com")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authService.updateOwnPassword(dto))
        .isInstanceOf(ModelNotFoundException.class)
        .hasMessageContaining("Usuário não encontrado");
  }

  @Test
  void shouldThrowWhenCurrentPasswordIsWrong() {
    UpdatePasswordDTO dto = new UpdatePasswordDTO("wrongPass", "newPass");
    SecurityContextHolder.setContext(new SecurityContextImpl(
        new UsernamePasswordAuthenticationToken(user.getEmail(), null)));

    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrongPass", user.getPassword())).thenReturn(false);

    assertThatThrownBy(() -> authService.updateOwnPassword(dto))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining("Senha atual incorreta");
  }

  @Test
  void shouldRegisterNewUserSuccessfully() {
    UserRequestDTO dto = new UserRequestDTO("Novo", "novo@teste.com", "senha", Role.ADMIN);
    when(userRepository.existsByEmail(dto.email())).thenReturn(false);
    when(userMapper.toEntity(dto)).thenReturn(user);
    when(passwordEncoder.encode("senha")).thenReturn("senhaCriptografada");
    when(userRepository.save(user)).thenReturn(user);

    String result = authService.register(dto);

    assertThat(result).isEqualTo("Usuário registrado com sucesso");
    verify(userRepository).save(user);
    assertThat(user.getRole()).isEqualTo(Role.ADMIN);
    assertThat(user.getPassword()).isEqualTo("senhaCriptografada");
  }

  @Test
  void shouldThrowWhenEmailAlreadyExists() {
    UserRequestDTO dto = new UserRequestDTO("Nome", "existe@teste.com", "senha", null);
    when(userRepository.existsByEmail(dto.email())).thenReturn(true);

    assertThatThrownBy(() -> authService.register(dto))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining("Email já cadastrado");
  }
}
