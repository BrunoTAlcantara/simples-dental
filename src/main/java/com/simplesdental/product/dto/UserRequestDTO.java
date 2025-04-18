package com.simplesdental.product.dto;

import com.simplesdental.product.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
    @NotBlank(message = "O nome do usuario é obrigatório")
    String name,
    @NotBlank(message = "O email do usuario é obrigatório")
    @Email(message = "O email do usuario deve ser válido")
    String email,
    @NotBlank(message = "A senha do usuario é obrigatório")
    String password,
    @NotNull(message = "O role do usuario é obrigatório")
    Role role
) {

}
