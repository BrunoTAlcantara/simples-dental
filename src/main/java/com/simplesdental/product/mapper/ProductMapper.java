package com.simplesdental.product.mapper;

import com.simplesdental.product.dto.ProductRequestDTO;
import com.simplesdental.product.dto.ProductResponseDTO;
import com.simplesdental.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

  @Mapping(target = "category", ignore = true)
  Product toEntity(ProductRequestDTO dto);

  @Mapping(target = "category", source = "category")
  ProductResponseDTO toResponseDTO(Product product);
}
