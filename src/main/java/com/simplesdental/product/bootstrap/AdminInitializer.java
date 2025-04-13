package com.simplesdental.product.bootstrap;

import com.simplesdental.product.enums.Role;
import com.simplesdental.product.model.User;
import com.simplesdental.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AdminProperties adminProperties;

  @Override
  public void run(String... args) {
    if (!userRepository.existsByEmail(adminProperties.getEmail())) {
      User admin = new User();
      admin.setName(adminProperties.getName());
      admin.setEmail(adminProperties.getEmail());
      admin.setPassword(passwordEncoder.encode(adminProperties.getPassword()));
      admin.setRole(Role.ADMIN);
      userRepository.save(admin);

      userRepository.save(admin);
      System.out.println("Usuário admin criado com sucesso.");
    } else {
      System.out.println("Usuário admin já existe.");
    }
  }
}