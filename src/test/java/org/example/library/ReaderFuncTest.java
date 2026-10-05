package org.example.library;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Библиотечный сервис")
@Feature("Читатели библиотеки")
public class ReaderFuncTest {
    @Test
    @Story("Просмотр читателей")
    @Description("Проверяет получение полного списка зарегистрированных читателей")
    @DisplayName("GET /api/readers — Все читатели")
    public void testGetAllReaders() {
        given()
                .when()
                .get("/api/readers")
                .then()
                .statusCode(200)
                .body("$", is(notNullValue()));
    }

    @Test
    @Story("Просмотр читателей")
    @Description("Проверяет получение профиля читателя по его уникальному ID")
    @DisplayName("GET /api/readers/{id} — Читатель по id")
    public void testGetReaderById() {
        given()
                .when()
                .get("/api/readers/{id}", 1L)
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    @Story("Поиск читателей")
    @Description("Проверяет поиск читателей по совпадению подстроки в фамилии")
    @DisplayName("GET /api/readers/search — Поиск читателей по фамилии")
    public void testSearchReadersByLastName() {
        given()
                .queryParam("lastName", "Иванов")
                .when()
                .get("/api/readers/search")
                .then()
                .statusCode(200)
                .body("lastName", hasItem(containsString("Иванов")));
    }

    @Test
    @Story("Статистика выдач")
    @Description("Проверяет подсчет количества книг, взятых конкретным читателем на указанную дату")
    @DisplayName("GET /api/readers/{readerId}/loans/count — Сколько книг взято читателем на дату")
    public void testCountOnDate() {
        given()
                .queryParam("date", "2026-10-05")
                .when()
                .get("/api/readers/{readerId}/loans/count", 1L)
                .then()
                .statusCode(200)
                .body("count", is(notNullValue()));
    }

    @Test
    @Story("Регистрация читателей")
    @Description("Проверяет успешное создание новой карточки читателя")
    @DisplayName("POST /api/readers — Создать читателя")
    public void testCreateReader() {
        String name = "Петр" + RandomStringUtils.randomAlphabetic(5);
        String surname = "Петров" + RandomStringUtils.randomAlphabetic(5);
        String card = "LIB-" + RandomStringUtils.randomAlphabetic(5);
        String email = "petr" + RandomStringUtils.randomAlphabetic(3) + "@vk.com";
        String requestBody = "{\n" +
                "  \"firstName\": \"" + name + "\",\n" +
                "  \"lastName\": \"" + surname + "\",\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"phone\": \"+7-983-789-4443\",\n" +
                "  \"libraryCard\": \"" + card + "\"\n" +
                "}";

        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/readers")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("lastName", equalTo(surname))
                .body("email", equalTo(email));
    }
}
