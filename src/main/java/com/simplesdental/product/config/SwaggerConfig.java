package com.simplesdental.product.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components().addSecuritySchemes("bearerAuth",
            new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        )).info(new Info()
            .title("API Simples Dental")
            .description("Documentação da API para gestão do stimples dental")
            .version("1.0"));
  }

  @Bean
  public GroupedOpenApi v1Group() {
    return GroupedOpenApi.builder()
        .group("v1")
        .pathsToMatch(
            "/api/categories/**",
            "/api/products/**",
            "/api/users/**",
            "/api/auth/**"
        )
        .build();
  }

  @Bean
  public GroupedOpenApi v2Group() {
    return GroupedOpenApi.builder()
        .group("v2")
        .pathsToMatch(
            "/api/v2/**"
        )
        .build();
  }
}
