package com.simplesdental.product.mapper;

import com.simplesdental.product.dto.CategoryRequestDTO;
import com.simplesdental.product.dto.CategoryResponseDTO;
import com.simplesdental.product.model.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-12T18:11:40-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CategoryRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Category category = new Category();

        category.setName( dto.name() );
        category.setDescription( dto.description() );

        return category;
    }

    @Override
    public CategoryResponseDTO toResponseDTO(Category category) {
        if ( category == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String description = null;

        id = category.getId();
        name = category.getName();
        description = category.getDescription();

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO( id, name, description );

        return categoryResponseDTO;
    }
}
