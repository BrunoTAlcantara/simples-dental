package com.simplesdental.product.controller;

import com.simplesdental.product.dto.ProductRequestDTO;
import com.simplesdental.product.dto.ProductResponseDTO;
import com.simplesdental.product.service.ProductService;
import com.simplesdental.product.shared.ResponsePageModel;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  @Transactional
  public ResponseEntity<ResponsePageModel<ProductResponseDTO>> getAllProducts(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    var result = productService.findAll(pageable);
    return ResponseEntity.ok(result);
  }


  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
    ProductResponseDTO product = productService.findById(id);
    return ResponseEntity.ok()
        .body(product);
  }

  @PostMapping
  public ResponseEntity<ProductResponseDTO> createProduct(
      @Valid @RequestBody ProductRequestDTO product) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(productService.save(product));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id,
      @Valid @RequestBody ProductRequestDTO product) {
    return ResponseEntity.ok(productService.update(id, product));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProduct(@PathVariable Long id) {
    productService.deleteById(id);
  }
}