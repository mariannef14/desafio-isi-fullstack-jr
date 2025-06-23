package com.isi.desafio_fullstack.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Desafio ISI Fullstack Jr",
        description = "Documentação para facilitar a compreensão da aplicação",
        version = "v1.0"
))
public class OpenApiConfig {
}