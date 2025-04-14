package com.simplesdental.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesdental.product.auth.JwtFilter;
import com.simplesdental.product.dto.CategoryRequestDTO;
import com.simplesdental.product.dto.CategoryResponseDTO;
import com.simplesdental.product.service.CategoryService;
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

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JwtFilter.class)
class CategoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CategoryService categoryService;

  @Autowired
  private ObjectMapper objectMapper;

  private CategoryRequestDTO requestDTO;
  private CategoryResponseDTO responseDTO;

  @BeforeEach
  void setUp() {
    requestDTO = new CategoryRequestDTO(
        "Categoria Teste",
        "Descrição da categoria"
    );

    responseDTO = new CategoryResponseDTO(
        1L,
        requestDTO.name(),
        requestDTO.description()
    );
  }

  @Test
  void shouldCreateCategory() throws Exception {
    when(categoryService.save(any(CategoryRequestDTO.class))).thenReturn(responseDTO);

    mockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(responseDTO.id()))
        .andExpect(jsonPath("$.name").value(responseDTO.name()));
  }

  @Test
  void shouldGetCategoryById() throws Exception {
    when(categoryService.findById(1L)).thenReturn(responseDTO);

    mockMvc.perform(get("/api/categories/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(responseDTO.id()))
        .andExpect(jsonPath("$.name").value(responseDTO.name()));
  }

  @Test
  void shouldUpdateCategory() throws Exception {
    when(categoryService.update(eq(1L), any(CategoryRequestDTO.class))).thenReturn(responseDTO);

    mockMvc.perform(put("/api/categories/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(responseDTO.id()))
        .andExpect(jsonPath("$.name").value(responseDTO.name()));
  }

  @Test
  void shouldDeleteCategory() throws Exception {
    doNothing().when(categoryService).deleteById(1L);

    mockMvc.perform(delete("/api/categories/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldGetPagedCategories() throws Exception {
    ResponsePageModel<CategoryResponseDTO> responsePage = new ResponsePageModel<>(
        1, 0, 1, List.of(responseDTO)
    );

    when(categoryService.findAll(any(PageRequest.class))).thenReturn(responsePage);

    mockMvc.perform(get("/api/categories?page=0&size=10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalItems").value(1))
        .andExpect(jsonPath("$.items[0].id").value(responseDTO.id()));
  }
}
