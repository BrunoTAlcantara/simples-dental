package com.simplesdental.product.mapper;

import com.simplesdental.product.dto.UserRequestDTO;
import com.simplesdental.product.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  User toEntity(UserRequestDTO dto);

  UserRequestDTO toResponseDTO(User user);
}
