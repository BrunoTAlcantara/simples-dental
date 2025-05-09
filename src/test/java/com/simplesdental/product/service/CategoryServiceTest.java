package com.simplesdental.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.simplesdental.product.dto.CategoryRequestDTO;
import com.simplesdental.product.dto.CategoryResponseDTO;
import com.simplesdental.product.mapper.CategoryMapper;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.repository.CategoryRepository;
import com.simplesdental.product.shared.ResponsePageModel;
import jakarta.persistence.EntityNotFoundException;
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
class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private CategoryMapper categoryMapper;

  @InjectMocks
  private CategoryService categoryService;

  private Category category;
  private CategoryRequestDTO requestDTO;
  private CategoryResponseDTO responseDTO;

  @BeforeEach
  void setUp() {
    requestDTO = new CategoryRequestDTO("Categoria Teste", "Descrição");
    responseDTO = new CategoryResponseDTO(1L, "Categoria Teste", "Descrição");

    category = new Category();
    category.setId(1L);
    category.setName(requestDTO.name());
    category.setDescription(requestDTO.description());
  }

  @Test
  void shouldReturnPagedCategoriesWithFilter() {
    Page<Category> page = new PageImpl<>(List.of(category));
    when(categoryRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);
    when(categoryMapper.toResponseDTO(category)).thenReturn(responseDTO);

    ResponsePageModel<CategoryResponseDTO> result = categoryService.findAll("Teste", PageRequest.of(0, 10));

    assertThat(result.totalItems()).isEqualTo(1);
    assertThat(result.items().get(0).name()).isEqualTo("Categoria Teste");
  }

  @Test
  void shouldFindCategoryById() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(categoryMapper.toResponseDTO(category)).thenReturn(responseDTO);

    CategoryResponseDTO result = categoryService.findById(1L);

    assertThat(result.name()).isEqualTo("Categoria Teste");
  }

  @Test
  void shouldThrowWhenCategoryNotFoundById() {
    when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> categoryService.findById(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Categoria 99");
  }

  @Test
  void shouldCreateCategory() {
    when(categoryMapper.toEntity(requestDTO)).thenReturn(category);
    when(categoryRepository.save(category)).thenReturn(category);
    when(categoryMapper.toResponseDTO(category)).thenReturn(responseDTO);

    CategoryResponseDTO result = categoryService.save(requestDTO);

    assertThat(result.name()).isEqualTo("Categoria Teste");
  }

  @Test
  void shouldUpdateCategory() {
    when(categoryRepository.existsById(1L)).thenReturn(true);
    when(categoryMapper.toEntity(requestDTO)).thenReturn(category);
    when(categoryRepository.save(category)).thenReturn(category);
    when(categoryMapper.toResponseDTO(category)).thenReturn(responseDTO);

    CategoryResponseDTO result = categoryService.update(1L, requestDTO);

    assertThat(result.id()).isEqualTo(1L);
    verify(categoryRepository).save(category);
  }

  @Test
  void shouldThrowWhenUpdatingNonexistentCategory() {
    when(categoryRepository.existsById(99L)).thenReturn(false);

    assertThatThrownBy(() -> categoryService.update(99L, requestDTO))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Categoria 99");
  }

  @Test
  void shouldDeleteCategory() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    doNothing().when(categoryRepository).delete(category);

    categoryService.deleteById(1L);

    verify(categoryRepository).delete(category);
  }

  @Test
  void shouldThrowWhenDeletingNonexistentCategory() {
    when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> categoryService.deleteById(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Categoria 99");
  }
}
