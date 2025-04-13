package com.simplesdental.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequestV2DTO(
    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(max = 100, message = "O nome do produto deve ter no máximo 100 caracteres")
    String name,

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    String description,

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    BigDecimal price,

    @NotNull(message = "O status é obrigatório")
    Boolean status,

    Integer code,

    @NotNull(message = "A categoria é obrigatória")
    Long categoryId
) {

}
