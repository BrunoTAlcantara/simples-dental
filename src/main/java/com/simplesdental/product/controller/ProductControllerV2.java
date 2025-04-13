package com.simplesdental.product.controller;

import com.simplesdental.product.dto.ProductRequestV2DTO;
import com.simplesdental.product.dto.ProductResponseV2DTO;
import com.simplesdental.product.service.ProductServiceV2;
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
import org.springframework.transaction.annotation.Transactional;
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

@Tag(name = "Products", description = "Endpoints para gerenciamento de produtos ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/products")
public class ProductControllerV2 {

  private final ProductServiceV2 productService;

  @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista paginada de produtos")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Produtos retornados com sucesso"),
      @ApiResponse(responseCode = "401", description = "Não autorizado"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @GetMapping
  @Transactional
  public ResponseEntity<ResponsePageModel<ProductResponseV2DTO>> getAllProducts(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    var result = productService.findAll(pageable);
    return ResponseEntity.ok(result);
  }

  @Operation(summary = "Buscar produto por ID", description = "Retorna os dados de um produto específico")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Produto encontrado"),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
      @ApiResponse(responseCode = "401", description = "Não autorizado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseV2DTO> getProductById(@PathVariable Long id) {
    ProductResponseV2DTO product = productService.findById(id);
    return ResponseEntity.ok().body(product);
  }

  @Operation(summary = "Criar novo produto", description = "Cria e retorna o produto salvo")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "401", description = "Não autorizado")
  })
  @PostMapping
  public ResponseEntity<ProductResponseV2DTO> createProduct(
      @Valid @RequestBody ProductRequestV2DTO product) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
  }

  @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos")
  })
  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseV2DTO> updateProduct(
      @PathVariable Long id,
      @Valid @RequestBody ProductRequestV2DTO product) {
    return ResponseEntity.ok(productService.update(id, product));
  }

  @Operation(summary = "Deletar produto", description = "Remove um produto por ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado")
  })
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProduct(@PathVariable Long id) {
    productService.deleteById(id);
  }
}
