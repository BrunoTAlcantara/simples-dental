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
import com.simplesdental.product.dto.ProductRequestV2DTO;
import com.simplesdental.product.dto.ProductResponseV2DTO;
import com.simplesdental.product.service.ProductServiceV2;
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

@WebMvcTest(ProductControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JwtFilter.class)
class ProductControllerV2Test {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ProductServiceV2 productService;

  private ProductRequestV2DTO requestDTO;
  private ProductResponseV2DTO responseDTO;

  @BeforeEach
  void setUp() {
    requestDTO = new ProductRequestV2DTO(
        "Produto V2",
        "Descrição V2",
        new BigDecimal("199.99"),
        true,
        123,
        1L
    );

    responseDTO = new ProductResponseV2DTO(
        1L,
        requestDTO.name(),
        requestDTO.description(),
        requestDTO.price(),
        requestDTO.status(),
        requestDTO.code(),
        null,
        null,
        null
    );
  }

  @Test
  void shouldCreateProductV2() throws Exception {
    when(productService.save(any(ProductRequestV2DTO.class))).thenReturn(responseDTO);

    mockMvc.perform(post("/api/v2/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(responseDTO.id()))
        .andExpect(jsonPath("$.name").value(responseDTO.name()));
  }

  @Test
  void shouldGetProductByIdV2() throws Exception {
    when(productService.findById(1L)).thenReturn(responseDTO);

    mockMvc.perform(get("/api/v2/products/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(responseDTO.id()))
        .andExpect(jsonPath("$.name").value(responseDTO.name()));
  }

  @Test
  void shouldUpdateProductV2() throws Exception {
    when(productService.update(eq(1L), any(ProductRequestV2DTO.class))).thenReturn(responseDTO);

    mockMvc.perform(put("/api/v2/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(responseDTO.id()))
        .andExpect(jsonPath("$.name").value(responseDTO.name()));
  }

  @Test
  void shouldDeleteProductV2() throws Exception {
    doNothing().when(productService).deleteById(1L);

    mockMvc.perform(delete("/api/v2/products/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldListPagedProductsV2WithoutFilter() throws Exception {
    ResponsePageModel<ProductResponseV2DTO> responsePage = new ResponsePageModel<>(
        1, 0, 1, List.of(responseDTO)
    );

    when(productService.findAll(eq(null), any(PageRequest.class))).thenReturn(responsePage);

    mockMvc.perform(get("/api/v2/products?page=0&size=10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalItems").value(1))
        .andExpect(jsonPath("$.items[0].id").value(responseDTO.id()));
  }

  @Test
  void shouldListPagedProductsV2WithNameFilter() throws Exception {
    ResponsePageModel<ProductResponseV2DTO> responsePage = new ResponsePageModel<>(
        1, 0, 1, List.of(responseDTO)
    );

    when(productService.findAll(eq("Produto V2"), any(PageRequest.class))).thenReturn(responsePage);

    mockMvc.perform(get("/api/v2/products")
            .param("page", "0")
            .param("size", "10")
            .param("name", "Produto V2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalItems").value(1))
        .andExpect(jsonPath("$.items[0].id").value(responseDTO.id()))
        .andExpect(jsonPath("$.items[0].name").value("Produto V2"));
  }
}
