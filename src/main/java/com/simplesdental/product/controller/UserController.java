package com.simplesdental.product.controller;

import com.simplesdental.product.dto.UserRequestDTO;
import com.simplesdental.product.dto.UserResponseDTO;
import com.simplesdental.product.service.UserService;
import com.simplesdental.product.shared.ResponsePageModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "Endpoints para gerenciamento de usuários")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @Operation(summary = "Listar usuários", description = "Retorna uma lista paginada de usuários")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso")
  })
  @GetMapping
  public ResponseEntity<ResponsePageModel<UserResponseDTO>> getAll(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return ResponseEntity.ok(userService.findAll(pageable));
  }

  @Operation(summary = "Buscar usuário por ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @Operation(summary = "Atualizar usuário")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
  })
  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody UserRequestDTO dto) {
    return ResponseEntity.ok(userService.update(id, dto));
  }

  @Operation(summary = "Deletar usuário")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
  })
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    userService.delete(id);
  }
}
