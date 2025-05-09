package com.simplesdental.product.controller;

import com.simplesdental.product.auth.UserDetailsImpl;
import com.simplesdental.product.dto.AuthRequestDTO;
import com.simplesdental.product.dto.AuthResponseDTO;
import com.simplesdental.product.dto.ContextResponseDTO;
import com.simplesdental.product.dto.UpdatePasswordDTO;
import com.simplesdental.product.dto.UserRequestDTO;
import com.simplesdental.product.service.AuthContextService;
import com.simplesdental.product.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Endpoints para autenticação e gerenciamento de credenciais")
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final AuthContextService authContextService;

  @Operation(summary = "Realizar login", description = "Autentica o usuário e retorna um token JWT")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
  })
  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário no sistema")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail já cadastrado")
  })
  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Valid UserRequestDTO dto) {
    return ResponseEntity.ok(authService.register(dto));
  }

  @Operation(summary = "Atualizar senha", description = "Atualiza a senha do usuário autenticado")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Senha atual incorreta ou inválida"),
      @ApiResponse(responseCode = "401", description = "Não autorizado")
  })
  @PutMapping("/password")
  public ResponseEntity<String> updateOwnPassword(@RequestBody @Valid UpdatePasswordDTO dto) {
    authService.updateOwnPassword(dto);
    return ResponseEntity.ok("Senha atualizada com sucesso");
  }

  @Operation(summary = "Obter contexto do usuário autenticado", description = "Retorna ID, e-mail e role do usuário logado")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Contexto carregado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Não autorizado")
  })
  @GetMapping("/context")
  public ContextResponseDTO getContext(@AuthenticationPrincipal UserDetailsImpl user) {
    return ResponseEntity.ok(authContextService.getContext(user.getEmail())).getBody();
  }
}
