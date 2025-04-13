package com.simplesdental.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesdental.product.auth.JwtFilter;
import com.simplesdental.product.dto.ProductRequestDTO;
import com.simplesdental.product.dto.ProductResponseDTO;
import com.simplesdental.product.service.ProductService;
import com.simplesdental.product.shared.ResponsePageModel;
import java.math.BigDecimal;
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

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JwtFilter.class)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService productService;

  @Autowired
  private ObjectMapper objectMapper;

  private ProductRequestDTO requestDTO;
  private ProductResponseDTO responseDTO;

  @BeforeEach
  void setUp() {
    requestDTO = new ProductRequestDTO(
        "Produto Teste",
        "Descrição do produto",
        new BigDecimal("99.99"),
        true,
        "PRD001",
        1L
    );

    responseDTO = new ProductResponseDTO(
        1L,
        requestDTO.name(),
        requestDTO.description(),
        requestDTO.price(),
        requestDTO.status(),
        requestDTO.code(),
        null, // createdAt
        null, // updatedAt
        null  // category
    );
  }

  @Test
  void shouldCreateProduct() throws Exception {
    when(productService.save(any(ProductRequestDTO.class))).thenReturn(responseDTO);

    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(responseDTO.id()))
        .andExpect(jsonPath("$.name").value(responseDTO.name()));
  }

  @Test
  void shouldGetProductById() throws Exception {
    when(productService.findById(1L)).thenReturn(responseDTO);

    mockMvc.perform(get("/api/products/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(responseDTO.id()))
        .andExpect(jsonPath("$.name").value(responseDTO.name()));
  }

  @Test
  void shouldUpdateProduct() throws Exception {
    when(productService.update(eq(1L), any(ProductRequestDTO.class))).thenReturn(responseDTO);

    mockMvc.perform(put("/api/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(responseDTO.id()))
        .andExpect(jsonPath("$.name").value(responseDTO.name()));
  }

  @Test
  void shouldDeleteProduct() throws Exception {
    doNothing().when(productService).deleteById(1L);

    mockMvc.perform(delete("/api/products/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldGetPagedProducts() throws Exception {
    ResponsePageModel<ProductResponseDTO> responsePage = new ResponsePageModel<>(
        1, 0, 1, List.of(responseDTO)
    );

    when(productService.findAll(any(PageRequest.class))).thenReturn(responsePage);

    mockMvc.perform(get("/api/products?page=0&size=10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalItems").value(1))
        .andExpect(jsonPath("$.items[0].id").value(responseDTO.id()));
  }
}
