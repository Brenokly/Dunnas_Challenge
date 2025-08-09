package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API de Gerenciamento de Pedidos e Pagamentos", version = "v1", description = "API para o desafio de seleção Fullstack Java da Dunnas Tecnologia."))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "Insira o token JWT obtido no endpoint de login.")
public class OpenApiConfig {
}