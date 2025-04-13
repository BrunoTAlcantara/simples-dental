package com.simplesdental.product.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordDTO(
    @NotBlank(message = "A senha atual é obrigatória")
    String currentPassword,
    @NotBlank(message = "A nova senha é obrigatória")
    String newPassword
) {

}
