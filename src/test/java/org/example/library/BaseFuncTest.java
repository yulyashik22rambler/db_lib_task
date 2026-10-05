package org.example.library;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
public abstract class BaseFuncTest {

    @BeforeAll
    public static void setUpBase() {
        // Берем URL из переменной окружения, либо используем локальный по умолчанию
        String baseUrl = System.getenv().getOrDefault("TEST_BASE_URL", "http://localhost");
        int port = Integer.parseInt(System.getenv().getOrDefault("TEST_PORT", "8080"));

        RestAssured.baseURI = baseUrl;
        RestAssured.port = port;

        // Настройка глобальной спецификации REST Assured
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured()) // Интеграция логов в Allure отчет
                .build();
    }
}