package com.hackerrank.productoapi.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger UI para exponer la especificación de la API.
 * <p>
 * Usa springdoc-openapi (2.x) compatible con Spring Boot 3. Al ejecutar la app,
 * la UI estará disponible en la ruta: {context-path}/swagger-ui/index.html
 * y el documento OpenAPI en: {context-path}/v3/api-docs (JSON) o
 * {context-path}/v3/api-docs.yaml (YAML).
 * </p>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Producto API",
                version = "1.0.0",
                description = "API para consultar y comparar productos."
        ),
        servers = {
                // Server relativo que respeta el context-path configurado en application.yml
                @Server(url = "${server.servlet.context-path}", description = "Servidor local con context-path")
        }
)
public class OpenApiConfig {
}
