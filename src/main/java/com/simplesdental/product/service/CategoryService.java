package com.simplesdental.product.service;

import com.simplesdental.product.dto.CategoryRequestDTO;
import com.simplesdental.product.dto.CategoryResponseDTO;
import com.simplesdental.product.logging.AppLogger;
import com.simplesdental.product.mapper.CategoryMapper;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.repository.CategoryRepository;
import com.simplesdental.product.shared.ResponsePageModel;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  @Transactional(readOnly = true)
  public ResponsePageModel<CategoryResponseDTO> findAll(Pageable pageable) {
    Page<Category> page = categoryRepository.findAll(pageable);
    var content = page.map(categoryMapper::toResponseDTO).getContent();
    AppLogger.info("Listagem de categorias consultada. Page=%d, Size=%d".formatted(page.getNumber(),
        page.getSize()));
    return new ResponsePageModel<>(page.getTotalElements(), page.getNumber(), page.getTotalPages(),
        content);
  }

  @Transactional(readOnly = true)
  public CategoryResponseDTO findById(Long id) {
    AppLogger.info("Categoria consultada. ID=" + id);
    return categoryRepository.findById(id)
        .map(categoryMapper::toResponseDTO)
        .orElseThrow(() -> new EntityNotFoundException("Categoria " + id + " não encontrada"));
  }

  @Transactional
  public CategoryResponseDTO save(CategoryRequestDTO categoryDto) {
    Category category = categoryMapper.toEntity(categoryDto);
    Category savedCategory = categoryRepository.save(category);
    AppLogger.info("Categoria criada com sucesso. ID=" + savedCategory.getId() + ", Nome="
        + savedCategory.getName());
    return categoryMapper.toResponseDTO(savedCategory);
  }

  @Transactional
  public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
    if (!categoryRepository.existsById(id)) {
      throw new EntityNotFoundException("Categoria " + id + " não encontrada");
    }

    Category category = categoryMapper.toEntity(dto);
    category.setId(id);
    Category updatedCategory = categoryRepository.save(category);

    AppLogger.info("Categoria atualizada. ID=" + id + ", Nome=" + updatedCategory.getName());
    return categoryMapper.toResponseDTO(updatedCategory);
  }

  @Transactional
  public void deleteById(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Categoria " + id + " não encontrada"));
    categoryRepository.delete(category);
    AppLogger.info("Categoria deletada. ID=" + id + ", Nome=" + category.getName());
  }

}