package com.simplesdental.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponseDTO(
    @Schema(description = "ID da categoria", example = "1")
    Long id,
    @Schema(description = "Nome da categoria", example = "Plantas")
    String name,
    @Schema(description = "Descrição da categoria", example = "Produtos relacionados à plantas")
    String description
) {

}
