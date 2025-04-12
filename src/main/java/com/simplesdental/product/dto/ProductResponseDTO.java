package com.simplesdental.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDTO(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Boolean status,
    String code,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime updatedAt,
    CategoryResponseDTO category
) {

}
