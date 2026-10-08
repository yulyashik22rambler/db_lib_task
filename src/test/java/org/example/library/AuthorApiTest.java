package org.example.library;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.library.dto.request.CreateAuthorRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Управление каталогом библиотеки")
@Feature("Авторы")
public class AuthorApiTest extends BaseFuncTest {

    @Test
    @Story("Создание автора")
    @Description("Проверяет успешное создание автора через POST и возврат структуры данных с ID")
    @DisplayName("POST /api/authors — Успешное создание")
    void testCreateAuthorSuccess() {
        // Используем ваш Record DTO
        String firstName = "Александр" + RandomStringUtils.randomAlphabetic(5);
        String lastName = "Пушкин";
        CreateAuthorRequest request = new CreateAuthorRequest(
                firstName,
                lastName,
                LocalDate.of(1799, 6, 6),
                "Россия"
        );

    //1. Ищем созданного автора
        Integer authorId = given()
                .body(request)
                .when()
                .post("/api/authors")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("firstName", equalTo(firstName))
                .body("lastName", equalTo(lastName))
                .body("id", notNullValue())
                .extract().path("id");

        // 2. Получение автора по ID (GET)
        given()
                .when()
                .get("/api/authors/{id}", authorId)
                .then()
                .statusCode(200)
                .body("id", equalTo(authorId.intValue()))
                .body("lastName", equalTo(lastName));

        // 3. Ищем созданного автора по префиксу фамилии
        given()
                .queryParam("lastName", "Пуш")
                .when()
                .get("/api/authors/search")
                .then()
                .statusCode(200)
                .body("lastName", hasItem("Пушкин"));

        // 4. Получение всех авторов (GET)
        given()
                .when()
                .get("/api/authors")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    @Story("Проверка валидации при создании автора")
    @Description("Отправка некорректного DTO (пустые строки и дата в будущем) должна возвращать 400 Bad Request")
    @DisplayName("POST /api/authors — Невалидное тело запроса")
    void testCreateAuthorValidationFailure() {
        // Создаем заведомо некорректный запрос (пустые имена и дата рождения в будущем)
        CreateAuthorRequest invalidRequest = new CreateAuthorRequest(
                "",
                "   ",
                LocalDate.now().plusYears(10),
                "Russia"
        );

        given()
                .body(invalidRequest)
                .when()
                .post("/api/authors")
                .then()
                .statusCode(400); // Ожидаем ошибку валидации от Spring фреймворка
    }
}