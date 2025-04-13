package com.simplesdental.product.service;

import com.simplesdental.product.dto.ProductRequestV2DTO;
import com.simplesdental.product.dto.ProductResponseV2DTO;
import com.simplesdental.product.exception.custom.ModelNotFoundException;
import com.simplesdental.product.logging.AppLogger;
import com.simplesdental.product.mapper.ProductMapper;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.repository.CategoryRepository;
import com.simplesdental.product.repository.ProductRepository;
import com.simplesdental.product.shared.ResponsePageModel;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceV2 {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public ResponsePageModel<ProductResponseV2DTO> findAll(Pageable pageable) {
    Page<Product> page = productRepository.findAll(pageable);
    AppLogger.info("Listagem de produtos consultada. Page=%d, Size=%d".formatted(page.getNumber(),
        page.getSize()));
    return new ResponsePageModel<>(
        page.getTotalElements(),
        page.getNumber(),
        page.getTotalPages(),
        page.map(productMapper::toV2ResponseDTO).getContent()
    );
  }

  @Transactional(readOnly = true)
  public ProductResponseV2DTO findById(Long id) {
    AppLogger.info("Produto consultado. ID=" + id);
    return productRepository.findById(id)
        .map(productMapper::toV2ResponseDTO)
        .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
  }

  @Transactional
  public ProductResponseV2DTO save(ProductRequestV2DTO ProductRequestV2DTO) {
    Category category = categoryRepository.findById(ProductRequestV2DTO.categoryId())
        .orElseThrow(() -> new ModelNotFoundException(
            "Categoria não encontrada", ProductRequestV2DTO.categoryId()));

    Product product = productMapper.V2toEntity(ProductRequestV2DTO);
    product.setCategory(category);

    Product savedProduct = productRepository.save(product);

    AppLogger.info("Produto criado. ID=" + savedProduct.getId()
        + ", Nome=" + savedProduct.getName()
        + ", Categoria=" + savedProduct.getCategory().getName());

    return productMapper.toV2ResponseDTO(savedProduct);
  }

  @Transactional
  public ProductResponseV2DTO update(Long id, ProductRequestV2DTO ProductRequestV2DTO) {
    if (!productRepository.existsById(id)) {
      throw new EntityNotFoundException("Produto " + id + " não encontrado");
    }

    Category category = categoryRepository.findById(ProductRequestV2DTO.categoryId())
        .orElseThrow(() -> new ModelNotFoundException("Categoria não encontrada",
            ProductRequestV2DTO.categoryId()));

    Product product = productMapper.V2toEntity(ProductRequestV2DTO);
    product.setId(id);
    product.setCategory(category);

    Product updatedProduct = productRepository.save(product);

    AppLogger.info("Produto atualizado. ID=" + id
        + ", Nome=" + updatedProduct.getName()
        + ", Categoria=" + updatedProduct.getCategory().getName());

    return productMapper.toV2ResponseDTO(updatedProduct);
  }

  @Transactional
  public void deleteById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
    productRepository.delete(product);
    AppLogger.info("Produto deletado. ID=" + id + ", Nome=" + product.getName());
  }
}
