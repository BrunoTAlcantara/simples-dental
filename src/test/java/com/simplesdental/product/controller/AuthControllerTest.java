package com.simplesdental.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesdental.product.auth.JwtFilter;
import com.simplesdental.product.auth.UserDetailsImpl;
import com.simplesdental.product.dto.AuthRequestDTO;
import com.simplesdental.product.dto.AuthResponseDTO;
import com.simplesdental.product.dto.ContextResponseDTO;
import com.simplesdental.product.dto.UpdatePasswordDTO;
import com.simplesdental.product.dto.UserRequestDTO;
import com.simplesdental.product.enums.Role;
import com.simplesdental.product.model.User;
import com.simplesdental.product.service.AuthContextService;
import com.simplesdental.product.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JwtFilter.class)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AuthService authService;

  @MockBean
  private AuthContextService authContextService;

  private AuthRequestDTO authRequest;
  private UserRequestDTO userRequest;
  private UpdatePasswordDTO updatePasswordDTO;
  private AuthResponseDTO authResponse;
  private ContextResponseDTO contextResponse;

  @BeforeEach
  void setUp() {
    authRequest = new AuthRequestDTO("user@test.com", "123456");
    userRequest = new UserRequestDTO("Novo Usuário", "user@test.com", "123456", Role.USER);
    updatePasswordDTO = new UpdatePasswordDTO("oldPass", "newPass");
    authResponse = new AuthResponseDTO("fake-jwt-token");
    contextResponse = new ContextResponseDTO(1L, "user@test.com", "USER");
  }

  @Test
  void shouldLoginSuccessfully() throws Exception {
    when(authService.login(any(AuthRequestDTO.class))).thenReturn(authResponse);

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("fake-jwt-token"));
  }

  @Test
  void shouldRegisterUser() throws Exception {
    when(authService.register(any(UserRequestDTO.class))).thenReturn("Usuário registrado com sucesso");

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string("Usuário registrado com sucesso"));
  }

  @Test
  void shouldUpdateOwnPassword() throws Exception {
    mockMvc.perform(put("/api/auth/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatePasswordDTO)))
        .andExpect(status().isOk())
        .andExpect(content().string("Senha atualizada com sucesso"));
  }

  @Test
  void shouldReturnUserContext() throws Exception {
    User user = new User();
    user.setId(1L);
    user.setEmail("user@test.com");
    user.setPassword("senha");
    user.setRole(Role.USER);

    UserDetailsImpl userDetails = new UserDetailsImpl(user);

    when(authContextService.getContext("user@test.com")).thenReturn(contextResponse);

    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
    );

    mockMvc.perform(get("/api/auth/context"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("user@test.com"))
        .andExpect(jsonPath("$.role").value("USER"));

    SecurityContextHolder.clearContext();
  }

}
