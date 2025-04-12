package com.simplesdental.product.mapper;

import com.simplesdental.product.dto.CategoryResponseDTO;
import com.simplesdental.product.dto.ProductRequestDTO;
import com.simplesdental.product.dto.ProductResponseDTO;
import com.simplesdental.product.model.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-12T17:47:38-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Product toEntity(ProductRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        product.setName( dto.name() );
        product.setDescription( dto.description() );
        product.setPrice( dto.price() );
        product.setStatus( dto.status() );
        product.setCode( dto.code() );

        return product;
    }

    @Override
    public ProductResponseDTO toResponseDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        CategoryResponseDTO category = null;
        Long id = null;
        String name = null;
        String description = null;
        BigDecimal price = null;
        Boolean status = null;
        String code = null;

        category = categoryMapper.toResponseDTO( product.getCategory() );
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        status = product.getStatus();
        code = product.getCode();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        ProductResponseDTO productResponseDTO = new ProductResponseDTO( id, name, description, price, status, code, createdAt, updatedAt, category );

        return productResponseDTO;
    }
}
