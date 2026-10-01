package org.example.library.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Настройка Swagger UI / OpenAPI 3.
 *
 * После запуска:
 *   Swagger UI:  http://localhost:8080/swagger-ui.html
 *   OpenAPI JSON: http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library Management API")
                        .description("""
                                REST API для городской библиотеки.                                
                                Возможности:
                                - Просмотр авторов и книг с жанрами
                                - Выдача и возврат книг
                                - История выдач, штрафы
                                - Отчёты по читателям
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Library Team")
                                .email("dev@library.local"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local dev")
                ))
                .tags(List.of(
                        new Tag().name("Authors").description("Управление авторами"),
                        new Tag().name("Books").description("Каталог книг"),
                        new Tag().name("Readers").description("Читатели библиотеки"),
                        new Tag().name("Loans").description("Выдача и возврат книг"),
                        new Tag().name("Fines").description("Штрафы")
                ));
    }
}