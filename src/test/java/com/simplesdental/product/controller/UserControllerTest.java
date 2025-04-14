package com.simplesdental.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesdental.product.auth.JwtFilter;
import com.simplesdental.product.dto.UserRequestDTO;
import com.simplesdental.product.dto.UserResponseDTO;
import com.simplesdental.product.enums.Role;
import com.simplesdental.product.service.UserService;
import com.simplesdental.product.shared.ResponsePageModel;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JwtFilter.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  private UserRequestDTO requestDTO;
  private UserResponseDTO responseDTO;

  @BeforeEach
  void setUp() {
    requestDTO = new UserRequestDTO("Bruno", "bruno@teste.com", "senha", Role.ADMIN);
    responseDTO = new UserResponseDTO(1L, "Bruno", "bruno@teste.com", Role.ADMIN);
  }

  @Test
  void shouldListPagedUsers() throws Exception {
    var responsePage = new ResponsePageModel<>(1, 0, 1, List.of(responseDTO));
    when(userService.findAll(any(PageRequest.class))).thenReturn(responsePage);

    mockMvc.perform(get("/api/users?page=0&size=10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalItems").value(1))
        .andExpect(jsonPath("$.items[0].id").value(1L))
        .andExpect(jsonPath("$.items[0].email").value("bruno@teste.com"));
  }

  @Test
  void shouldGetUserById() throws Exception {
    when(userService.findById(1L)).thenReturn(responseDTO);

    mockMvc.perform(get("/api/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.email").value("bruno@teste.com"));
  }

  @Test
  void shouldUpdateUser() throws Exception {
    when(userService.update(any(Long.class), any(UserRequestDTO.class))).thenReturn(responseDTO);

    mockMvc.perform(put("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Bruno"));
  }

  @Test
  void shouldDeleteUser() throws Exception {
    doNothing().when(userService).delete(1L);

    mockMvc.perform(delete("/api/users/1"))
        .andExpect(status().isNoContent());
  }
}
