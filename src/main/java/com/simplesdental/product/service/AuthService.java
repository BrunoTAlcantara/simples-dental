package com.simplesdental.product.service;

import com.simplesdental.product.auth.JwtUtil;
import com.simplesdental.product.dto.AuthRequestDTO;
import com.simplesdental.product.dto.AuthResponseDTO;
import com.simplesdental.product.dto.UpdatePasswordDTO;
import com.simplesdental.product.dto.UserRequestDTO;
import com.simplesdental.product.enums.Role;
import com.simplesdental.product.exception.custom.BusinessException;
import com.simplesdental.product.exception.custom.ModelNotFoundException;
import com.simplesdental.product.logging.AppLogger;
import com.simplesdental.product.mapper.UserMapper;
import com.simplesdental.product.model.User;
import com.simplesdental.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public AuthResponseDTO login(AuthRequestDTO request) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password())
    );

    UserDetails userDetails = (UserDetails) auth.getPrincipal();
    String token = jwtUtil.generateToken(userDetails);

    AppLogger.info("Login realizado com sucesso para o e-mail: " + request.email());

    return new AuthResponseDTO(token);
  }

  @CacheEvict(value = "userContext", key = "#email")
  @Transactional
  public void updateOwnPassword(UpdatePasswordDTO dto) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ModelNotFoundException("Usuário não encontrado", null));

    if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
      throw new BusinessException("Senha atual incorreta");
    }

    user.setPassword(passwordEncoder.encode(dto.newPassword()));
    userRepository.save(user);

    AppLogger.info("Senha atualizada com sucesso. Email=" + email);
  }

  public String register(UserRequestDTO dto) {
    if (userRepository.existsByEmail(dto.email())) {
      throw new BusinessException("Email já cadastrado");
    }

    User user = userMapper.toEntity(dto);
    user.setPassword(passwordEncoder.encode(dto.password()));
    user.setRole(dto.role() != null ? dto.role() : Role.USER);

    userRepository.save(user);

    AppLogger.info(
        "Novo usuário registrado. Email=" + user.getEmail() + ", Role=" + user.getRole());

    return "Usuário registrado com sucesso";
  }
}
