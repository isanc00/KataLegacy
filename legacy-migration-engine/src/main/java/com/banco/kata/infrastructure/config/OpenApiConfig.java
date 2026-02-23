package com.banco.kata.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Migración: Legacy2Modern Engine")
                        .version("1.0.0")
                        .description("Motor de análisis sintáctico (AST) para la migración automatizada de código legacy a Java.")
                        .contact(new Contact()
                                .name("CoE de Desarrollo - Arquitectura Cloud")
                                .email("arquitectura@banco.com")));
    }
}
