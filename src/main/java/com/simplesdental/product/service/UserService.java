package com.simplesdental.product.service;

import com.simplesdental.product.dto.UserRequestDTO;
import com.simplesdental.product.dto.UserResponseDTO;
import com.simplesdental.product.exception.custom.ModelNotFoundException;
import com.simplesdental.product.logging.AppLogger;
import com.simplesdental.product.mapper.UserMapper;
import com.simplesdental.product.model.User;
import com.simplesdental.product.repository.UserRepository;
import com.simplesdental.product.shared.ResponsePageModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserResponseDTO update(Long id, UserRequestDTO dto) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ModelNotFoundException("Usuário " + id + " não encontrado", null));

    user.setName(dto.name());
    user.setEmail(dto.email());
    user.setPassword(passwordEncoder.encode(dto.password()));
    user.setRole(dto.role());

    User updated = userRepository.save(user);
    AppLogger.info("Usuário atualizado. ID=" + updated.getId() + ", Email=" + updated.getEmail());
    return userMapper.toResponseDTO(updated);
  }

  @Transactional
  public void delete(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ModelNotFoundException("Usuário " + id + " não encontrado", null));
    userRepository.delete(user);
    AppLogger.info("Usuário deletado. ID=" + id + ", Email=" + user.getEmail());
  }

  @Transactional
  public UserResponseDTO findById(Long id) {
    return userRepository.findById(id)
        .map(userMapper::toResponseDTO)
        .orElseThrow(() -> new ModelNotFoundException("Usuário " + id + " não encontrado", null));
  }

  @Transactional
  public ResponsePageModel<UserResponseDTO> findAll(Pageable pageable) {
    Page<User> page = userRepository.findAll(pageable);
    var content = page.map(userMapper::toResponseDTO).getContent();
    AppLogger.info("Listagem de usuários. Page=%d, Size=%d".formatted(page.getNumber(), page.getSize()));
    return new ResponsePageModel<>(page.getTotalElements(), page.getNumber(), page.getTotalPages(), content);
  }
}
