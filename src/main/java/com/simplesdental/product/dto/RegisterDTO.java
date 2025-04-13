package com.simplesdental.product.dto;

import com.simplesdental.product.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
    @NotBlank(message = "O nome é obrigatório")
    String name,
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    String email,
    @NotBlank(message = "A senha é obrigatório")
    String password,
    @NotNull(message = "O role é obrigatório")
    Role role
) {

}
