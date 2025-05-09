package com.simplesdental.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.simplesdental.product.dto.ProductRequestDTO;
import com.simplesdental.product.dto.ProductResponseDTO;
import com.simplesdental.product.exception.custom.ModelNotFoundException;
import com.simplesdental.product.mapper.ProductMapper;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.repository.CategoryRepository;
import com.simplesdental.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
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
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private ProductMapper productMapper;

  @InjectMocks
  private ProductService productService;

  private Product product;
  private Category category;
  private ProductRequestDTO requestDTO;
  private ProductResponseDTO responseDTO;

  @BeforeEach
  void setUp() {
    requestDTO = new ProductRequestDTO("Produto Teste", "Desc", new BigDecimal("99.99"), true, "PRD001", 1L);
    responseDTO = new ProductResponseDTO(1L, "Produto Teste", "Desc", new BigDecimal("99.99"), true, "PRD001", null, null, null);

    category = new Category();
    category.setId(1L);
    category.setName("Categoria Teste");

    product = new Product();
    product.setId(1L);
    product.setName(requestDTO.name());
    product.setDescription(requestDTO.description());
    product.setPrice(requestDTO.price());
    product.setStatus(requestDTO.status());
    product.setCode(requestDTO.code());
    product.setCategory(category);
  }

  @Test
  void shouldSaveProduct() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(productMapper.toEntity(requestDTO)).thenReturn(product);
    when(productRepository.save(product)).thenReturn(product);
    when(productMapper.toResponseDTO(product)).thenReturn(responseDTO);

    ProductResponseDTO result = productService.save(requestDTO);

    assertThat(result).isNotNull();
    assertThat(result.name()).isEqualTo("Produto Teste");
  }

  @Test
  void shouldThrowWhenCategoryNotFound() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.save(requestDTO))
        .isInstanceOf(ModelNotFoundException.class)
        .hasMessageContaining("Categoria não encontrada");
  }

  @Test
  void shouldReturnPagedProductsWithFilter() {
    Page<Product> page = new PageImpl<>(List.of(product));
    when(productRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);
    when(productMapper.toResponseDTO(product)).thenReturn(responseDTO);

    var result = productService.findAll("Produto", PageRequest.of(0, 10));

    assertThat(result.totalItems()).isEqualTo(1);
    assertThat(result.items().get(0).name()).isEqualTo("Produto Teste");
  }

  @Test
  void shouldFindProductById() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(productMapper.toResponseDTO(product)).thenReturn(responseDTO);

    ProductResponseDTO result = productService.findById(1L);

    assertThat(result.name()).isEqualTo("Produto Teste");
  }

  @Test
  void shouldThrowWhenProductNotFoundById() {
    when(productRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.findById(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Produto não encontrado");
  }

  @Test
  void shouldUpdateProduct() {
    when(productRepository.existsById(1L)).thenReturn(true);
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(productMapper.toEntity(requestDTO)).thenReturn(product);
    when(productRepository.save(product)).thenReturn(product);
    when(productMapper.toResponseDTO(product)).thenReturn(responseDTO);

    ProductResponseDTO result = productService.update(1L, requestDTO);

    assertThat(result.id()).isEqualTo(1L);
    verify(productRepository).save(product);
  }

  @Test
  void shouldThrowWhenUpdateProductNotFound() {
    when(productRepository.existsById(99L)).thenReturn(false);

    assertThatThrownBy(() -> productService.update(99L, requestDTO))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Produto 99");
  }

  @Test
  void shouldDeleteProduct() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    doNothing().when(productRepository).delete(product);

    productService.deleteById(1L);

    verify(productRepository).delete(product);
  }

  @Test
  void shouldThrowWhenDeleteProductNotFound() {
    when(productRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.deleteById(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Produto não encontrado");
  }

  @Test
  void shouldThrowModelNotFoundWhenCategoryNotExistsOnSave() {
    when(categoryRepository.findById(requestDTO.categoryId()))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.save(requestDTO))
        .isInstanceOf(ModelNotFoundException.class)
        .hasMessageContaining("Categoria não encontrada");

    verify(categoryRepository).findById(requestDTO.categoryId());
  }

  @Test
  void shouldThrowModelNotFoundWhenCategoryNotExistsOnUpdate() {
    when(productRepository.existsById(1L)).thenReturn(true);
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.update(1L, requestDTO))
        .isInstanceOf(ModelNotFoundException.class)
        .hasMessageContaining("Categoria não encontrada");

    verify(categoryRepository).findById(1L);
  }
}
