package com.simplesdental.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.simplesdental.product.dto.ProductRequestV2DTO;
import com.simplesdental.product.dto.ProductResponseV2DTO;
import com.simplesdental.product.exception.custom.ModelNotFoundException;
import com.simplesdental.product.mapper.ProductMapper;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.repository.CategoryRepository;
import com.simplesdental.product.repository.ProductRepository;
import com.simplesdental.product.shared.ResponsePageModel;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ProductServiceV2Test {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private ProductMapper productMapper;

  @InjectMocks
  private ProductServiceV2 productService;

  private Product product;
  private Category category;
  private ProductRequestV2DTO requestDTO;
  private ProductResponseV2DTO responseDTO;

  @BeforeEach
  void setUp() {
    category = new Category();
    category.setId(1L);
    category.setName("Categoria Teste");

    product = new Product();
    product.setId(1L);
    product.setName("Produto Teste");
    product.setDescription("Desc");
    product.setPrice(new BigDecimal("99.99"));
    product.setStatus(true);
    product.setCode("123");
    product.setCategory(category);

    requestDTO = new ProductRequestV2DTO("Produto Teste", "Desc", new BigDecimal("99.99"), true, 123, 1L);

    responseDTO = new ProductResponseV2DTO(1L, "Produto Teste", "Desc", new BigDecimal("99.99"), true, 123, null, null, null);
  }

  @Test
  void shouldListPagedProductsWithFilter() {
    Page<Product> page = new PageImpl<>(List.of(product));
    when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
    when(productMapper.toV2ResponseDTO(product)).thenReturn(responseDTO);

    ResponsePageModel<ProductResponseV2DTO> result = productService.findAll("Produto", PageRequest.of(0, 10));

    assertThat(result.totalItems()).isEqualTo(1);
    assertThat(result.items().get(0).name()).isEqualTo("Produto Teste");
  }

  @Test
  void shouldFindProductById() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(productMapper.toV2ResponseDTO(product)).thenReturn(responseDTO);

    ProductResponseV2DTO result = productService.findById(1L);

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
  void shouldSaveProduct() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(productMapper.V2toEntity(requestDTO)).thenReturn(product);
    when(productRepository.save(product)).thenReturn(product);
    when(productMapper.toV2ResponseDTO(product)).thenReturn(responseDTO);

    ProductResponseV2DTO result = productService.save(requestDTO);

    assertThat(result.name()).isEqualTo("Produto Teste");
    verify(productRepository).save(product);
  }

  @Test
  void shouldThrowWhenCategoryNotFoundOnSave() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.save(requestDTO))
        .isInstanceOf(ModelNotFoundException.class)
        .hasMessageContaining("Categoria não encontrada");
  }

  @Test
  void shouldUpdateProduct() {
    when(productRepository.existsById(1L)).thenReturn(true);
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(productMapper.V2toEntity(requestDTO)).thenReturn(product);
    when(productRepository.save(product)).thenReturn(product);
    when(productMapper.toV2ResponseDTO(product)).thenReturn(responseDTO);

    ProductResponseV2DTO result = productService.update(1L, requestDTO);

    assertThat(result.id()).isEqualTo(1L);
    verify(productRepository).save(product);
  }

  @Test
  void shouldThrowWhenProductNotFoundOnUpdate() {
    when(productRepository.existsById(99L)).thenReturn(false);

    assertThatThrownBy(() -> productService.update(99L, requestDTO))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Produto 99");
  }

  @Test
  void shouldThrowWhenCategoryNotFoundOnUpdate() {
    when(productRepository.existsById(1L)).thenReturn(true);
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.update(1L, requestDTO))
        .isInstanceOf(ModelNotFoundException.class)
        .hasMessageContaining("Categoria não encontrada");
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
}
