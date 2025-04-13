package com.simplesdental.product.mapper;

import com.simplesdental.product.dto.ProductRequestDTO;
import com.simplesdental.product.dto.ProductRequestV2DTO;
import com.simplesdental.product.dto.ProductResponseDTO;
import com.simplesdental.product.dto.ProductResponseV2DTO;
import com.simplesdental.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

  // ===== V1 =====
  @Mapping(target = "category", ignore = true)
  Product toEntity(ProductRequestDTO dto);

  ProductResponseDTO toResponseDTO(Product product);

  // ===== V2 =====
  @Mapping(target = "category", ignore = true)
  Product V2toEntity(ProductRequestV2DTO dto);


  ProductResponseV2DTO toV2ResponseDTO(Product product);
}
