package com.simplesdental.product.controller;

import com.simplesdental.product.dto.CategoryRequestDTO;
import com.simplesdental.product.dto.CategoryResponseDTO;
import com.simplesdental.product.service.CategoryService;
import com.simplesdental.product.shared.ResponsePageModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Categories", description = "Endpoints para gerenciamento de categorias")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @Operation(summary = "Listar categorias", description = "Retorna uma lista paginada de categorias")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Categorias retornadas com sucesso"),
      @ApiResponse(responseCode = "401", description = "Não autorizado"),
      @ApiResponse(responseCode = "500", description = "Erro interno")
  })
  @GetMapping
  public ResponseEntity<ResponsePageModel<CategoryResponseDTO>> getAllCategories(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(required = false) String name,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    ResponsePageModel<CategoryResponseDTO> result = categoryService.findAll(name, pageable);
    return ResponseEntity.ok(result);
  }

  @Operation(summary = "Buscar categoria por ID", description = "Retorna uma categoria pelo ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
      @ApiResponse(responseCode = "401", description = "Não autorizado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
    CategoryResponseDTO category = categoryService.findById(id);
    return ResponseEntity.ok().body(category);
  }

  @Operation(summary = "Criar nova categoria", description = "Cria e retorna uma nova categoria")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "401", description = "Não autorizado")
  })
  @PostMapping
  public ResponseEntity<CategoryResponseDTO> createCategory(
      @Valid @RequestBody CategoryRequestDTO category) {
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
  }

  @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria existente")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos")
  })
  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponseDTO> updateCategories(
      @PathVariable Long id,
      @Valid @RequestBody CategoryRequestDTO category) {
    return ResponseEntity.ok(categoryService.update(id, category));
  }

  @Operation(summary = "Deletar categoria", description = "Remove uma categoria pelo ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
  })
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCategory(@PathVariable Long id) {
    categoryService.deleteById(id);
  }
}
