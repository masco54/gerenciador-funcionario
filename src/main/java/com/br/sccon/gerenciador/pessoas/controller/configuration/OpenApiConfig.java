package com.br.sccon.gerenciador.pessoas.controller.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SCCON - Gestão de Pessoas")
                        .version("1.0.0")
                        .description("API REST para gestão de pessoas, implementando todos os requisitos de CRUD e lógica de negócio com tratamento de erros."));
    }
}
