package com.simplesdental.product.mapper;

import com.simplesdental.product.dto.CategoryRequestDTO;
import com.simplesdental.product.dto.CategoryResponseDTO;
import com.simplesdental.product.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  Category toEntity(CategoryRequestDTO dto);

  CategoryResponseDTO toResponseDTO(Category category);
}
