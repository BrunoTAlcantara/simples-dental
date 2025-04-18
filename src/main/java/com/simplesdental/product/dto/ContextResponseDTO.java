package com.simplesdental.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações do usuário autenticado")
public record ContextResponseDTO(

    @Schema(description = "ID do usuário", example = "42")
    Long id,

    @Schema(description = "E-mail do usuário autenticado", example = "joao@email.com")
    String email,

    @Schema(description = "Papel (role) do usuário", example = "USER")
    String role

) {

}
