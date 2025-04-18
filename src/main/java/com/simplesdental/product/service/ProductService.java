package com.simplesdental.product.service;

import com.simplesdental.product.dto.ProductRequestDTO;
import com.simplesdental.product.dto.ProductResponseDTO;
import com.simplesdental.product.exception.custom.ModelNotFoundException;
import com.simplesdental.product.logging.AppLogger;
import com.simplesdental.product.mapper.ProductMapper;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.repository.CategoryRepository;
import com.simplesdental.product.repository.ProductRepository;
import com.simplesdental.product.repository.specifications.ProductSpecification;
import com.simplesdental.product.shared.ResponsePageModel;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public ResponsePageModel<ProductResponseDTO> findAll(String name, Pageable pageable) {
    Specification<Product> spec = Specification
        .where(ProductSpecification.nameContains(name));

    Page<Product> page = productRepository.findAll(spec, pageable);
    var content = page.map(productMapper::toResponseDTO).getContent();

    AppLogger.info("Listagem de produtos com filtro. Page=%d, Size=%d".formatted(page.getNumber(), page.getSize()));
    return new ResponsePageModel<>(page.getTotalElements(), page.getNumber(), page.getTotalPages(), content);
  }

  @Transactional(readOnly = true)
  public ProductResponseDTO findById(Long id) {
    AppLogger.info("Produto consultado. ID=" + id);
    return productRepository.findById(id)
        .map(productMapper::toResponseDTO)
        .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
  }

  @Transactional
  public ProductResponseDTO save(ProductRequestDTO productRequestDTO) {
    Category category = categoryRepository.findById(productRequestDTO.categoryId())
        .orElseThrow(() -> new ModelNotFoundException(
            "Categoria não encontrada", productRequestDTO.categoryId()));

    Product product = productMapper.toEntity(productRequestDTO);
    product.setCategory(category);

    Product savedProduct = productRepository.save(product);

    AppLogger.info("Produto criado. ID=" + savedProduct.getId()
        + ", Nome=" + savedProduct.getName()
        + ", Categoria=" + savedProduct.getCategory().getName());

    return productMapper.toResponseDTO(savedProduct);
  }

  @Transactional
  public ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO) {
    if (!productRepository.existsById(id)) {
      throw new EntityNotFoundException("Produto " + id + " não encontrado");
    }

    Category category = categoryRepository.findById(productRequestDTO.categoryId())
        .orElseThrow(() -> new ModelNotFoundException("Categoria não encontrada",
            productRequestDTO.categoryId()));

    Product product = productMapper.toEntity(productRequestDTO);
    product.setId(id);
    product.setCategory(category);

    Product updatedProduct = productRepository.save(product);

    AppLogger.info("Produto atualizado. ID=" + id
        + ", Nome=" + updatedProduct.getName()
        + ", Categoria=" + updatedProduct.getCategory().getName());

    return productMapper.toResponseDTO(updatedProduct);
  }

  @Transactional
  public void deleteById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
    productRepository.delete(product);
    AppLogger.info("Produto deletado. ID=" + id + ", Nome=" + product.getName());
  }
}
