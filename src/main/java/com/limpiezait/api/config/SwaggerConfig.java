package com.limpiezait.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    private static final String ESQUEMA = "bearerAuth";

    @Bean
    public OpenAPI limpiezaItOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LimpiezaIT API")
                        .version("1.0.0")
                        .description(
                                "API REST para la gestión de productos de limpieza. Curso Java Backend, educaciónIT.")
                        .contact(new Contact().name("Lic. Carmine Fernando")
                                .url("https://www.linkedin.com/in/carminefernando/")))
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA))
                .components(new Components().addSecuritySchemes(ESQUEMA,
                        new SecurityScheme()
                                .name(ESQUEMA)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}