package com.simplesdental.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados do produto retornados pela API")
public record ProductResponseV2DTO(

    @Schema(description = "ID do produto", example = "10")
    Long id,

    @Schema(description = "Nome do produto", example = "Escova Ortodôntica")
    String name,

    @Schema(description = "Descrição do produto", example = "Escova com cerdas especiais para uso com aparelho")
    String description,

    @Schema(description = "Preço do produto", example = "29.90")
    BigDecimal price,

    @Schema(description = "Indica se o produto está ativo", example = "true")
    Boolean status,

    @Schema(description = "Código do produto (formato 10)", example = "10")
    Integer code,

    @Schema(description = "Data de criação", example = "13-04-2025 15:23:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime createdAt,

    @Schema(description = "Data da última atualização", example = "13-04-2025 18:47:21")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime updatedAt,

    @Schema(description = "Categoria do produto")
    CategoryResponseDTO category

) {

}
