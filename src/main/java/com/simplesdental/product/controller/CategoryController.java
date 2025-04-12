package com.simplesdental.product.controller;

import com.simplesdental.product.dto.CategoryRequestDTO;
import com.simplesdental.product.dto.CategoryResponseDTO;
import com.simplesdental.product.service.CategoryService;
import com.simplesdental.product.shared.ResponsePageModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<ResponsePageModel<CategoryResponseDTO>> getAllCategories(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    ResponsePageModel<CategoryResponseDTO> result = categoryService.findAll(pageable);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
    CategoryResponseDTO category = categoryService.findById(id);
    return ResponseEntity.ok()
        .body(category);
  }

  @PostMapping
  public ResponseEntity<CategoryResponseDTO> createCategory(
      @Valid @RequestBody CategoryRequestDTO category) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(categoryService.save(category));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponseDTO> updateCategories(
      @PathVariable Long id,
      @Valid @RequestBody CategoryRequestDTO category
  ) {
    return ResponseEntity.ok(categoryService.update(id, category));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCategory(@PathVariable Long id) {
    categoryService.deleteById(id);
  }
}