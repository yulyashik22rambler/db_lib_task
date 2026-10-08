package org.example.library;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("Управление каталогом библиотеки")
@Feature("Авторы и Книги")
public class BookApiTest extends BaseFuncTest {
    @Test
    @Story("Поиск доступных книг")
    @Description("Проверяет получение списка книг, доступных к выдаче читателям")
    @DisplayName("GET /api/books/available — Получение доступных книг")
    void testGetAvailableBooks() {
        given()
                .when()
                .get("/api/books/available")
                .then()
                .statusCode(200)
                .body("$", is(notNullValue())); // Проверяем, что возвращается список
    }


    @Test
    @Story("Просмотр каталога")
    @Description("Проверяет получение всех книг с подробной информацией об авторах и жанрах")
    @DisplayName("GET /api/books — Все книги с авторами и жанрами")
    public void testGetAllBooksDetailed() {
        given()
                .when()
                .get("/api/books")
                .then()
                .statusCode(200)
                .body("$", is(notNullValue()));
    }

    @Test
    @Story("Просмотр каталога")
    @Description("Проверяет получение информации о книге по её идентификатору")
    @DisplayName("GET /api/books/{id} — Книга по id")
    public void testGetBookById() {
        given()
                .when()
                .get("/api/books/{id}", 1L)
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    @Story("Поиск книг")
    @Description("Проверяет поиск книг по подстроке в названии")
    @DisplayName("GET /api/books/search — Поиск книг по названию")
    public void testSearchByTitle() {
        given()
                .queryParam("title", "Анна Каренина")
                .when()
                .get("/api/books/search")
                .then()
                .statusCode(200)
                .body("title", hasItem(containsString("Анна Каренина")));
    }

    @Test
    @Story("Поиск книг")
    @Description("Проверяет получение списка книг определенного автора по его фамилии")
    @DisplayName("GET /api/books/by-author — Поиск книг по фамилии автора")
    public void testFindByAuthorLastName() {
        given()
                .queryParam("lastName", "Tolstoy")
                .when()
                .get("/api/books/by-author")
                .then()
                .statusCode(200)
                .body("$", is(notNullValue()));
    }

    @Test
    @Story("Поиск книг")
    @Description("Проверяет фильтрацию книг по названию жанра")
    @DisplayName("GET /api/books/by-genre — Поиск книг по жанру")
    public void testFindByGenre() {
        given()
                .queryParam("name", "Роман")
                .when()
                .get("/api/books/by-genre")
                .then()
                .statusCode(200)
                .body("$", is(notNullValue()));
    }

    @Test
    @Story("История книги")
    @Description("Проверяет получение полной истории выдачи конкретной книги (все записи loan)")
    @DisplayName("GET /api/books/{id}/history — История выдачи книги")
    public void testGetBookHistory() {
        given()
                .when()
                .get("/api/books/{id}/history", 1L)
                .then()
                .statusCode(200)
                .body("$", is(notNullValue()));
    }

    @Test
    @Story("Регистрация книг")
    @Description("Проверяет успешное добавление новой книги в каталог библиотеки")
    @DisplayName("POST /api/books — Создать книгу")
    public void testCreateBook() {
        Map<String, Object> requestBody = Map.of(
                "title", "Война и мир",
                "authorIds", List.of(1L),
                "genreId", 2L
        );

        given()
                .body(requestBody)
                .when()
                .post("/api/books")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("title", equalTo("Война и мир"));
    }
}
