package com.simplesdental.product.dto;

import com.simplesdental.product.enums.Role;

public record ContextResponseDTO(
    Long id,
    String email,
    Role role
) {

}