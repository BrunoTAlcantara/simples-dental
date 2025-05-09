package com.simplesdental.product.dto;

import com.simplesdental.product.enums.Role;

public record UserResponseDTO(
    Long id,
    String name,
    String email,
    Role role
) {

}
