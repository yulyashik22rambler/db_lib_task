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

@Epic("Библиотечный сервис")
@Feature("Выдача и возврат книг")
public class LoanFuncTest {
    @Test
    @Story("Активные выдачи")
    @Description("Проверяет получение списка всех книг, которые находятся на руках у читателей в данный момент")
    @DisplayName("GET /api/loans/active — Активные выдачи (книги на руках)")
    public void testGetActiveLoans() {
        given()
                .when()
                .get("/api/loans/active")
                .then()
                .statusCode(200)
                .body("$", is(notNullValue()));
    }

    @Test
    @Story("Регистрация операций")
    @Description("Проверяет успешное оформление выдачи книги читателю")
    @DisplayName("POST /api/loans/issue — Выдать книгу читателю")
    public void testIssueBook() {
        Map<String, Object> requestBody = Map.of(
                "readerId", 1L,
                "bookId", 5L
        );

        given()
                .body(requestBody)
                .when()
                .post("/api/loans/issue")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()));
    }

    @Test
    @Story("Регистрация операций")
    @Description("Проверяет успешное оформление возврата книги в библиотеку")
    @DisplayName("POST /api/loans/return — Вернуть книгу")
    public void testReturnBook() {
        Map<String, Object> requestBody = Map.of(
                "loanId", 2L
        );

        given()
                .body(requestBody)
                .when()
                .post("/api/loans/return")
                .then()
                .statusCode(200)
                .body("id", equalTo(2));
    }
}
