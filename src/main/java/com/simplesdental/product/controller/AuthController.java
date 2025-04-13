package com.simplesdental.product.controller;


import com.simplesdental.product.auth.UserDetailsImpl;
import com.simplesdental.product.dto.AuthRequestDTO;
import com.simplesdental.product.dto.AuthResponseDTO;
import com.simplesdental.product.dto.ContextResponseDTO;
import com.simplesdental.product.dto.UpdatePasswordDTO;
import com.simplesdental.product.dto.UserRequestDTO;
import com.simplesdental.product.service.AuthService;
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

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Valid UserRequestDTO dto) {
    return ResponseEntity.ok(authService.register(dto));
  }

  @PutMapping("/password")
  public ResponseEntity<String> updateOwnPassword(@RequestBody @Valid UpdatePasswordDTO dto) {
    authService.updateOwnPassword(dto);
    return ResponseEntity.ok("Senha atualizada com sucesso");
  }

  @GetMapping("/context")
  public ContextResponseDTO getContext(@AuthenticationPrincipal UserDetailsImpl user) {
    return new ContextResponseDTO(user.getId(), user.getEmail(), "USER");
  }
}
