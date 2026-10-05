package org.example.library;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("Библиотечный сервис")
@Feature("Штрафы")
public class FineFuncTest extends BaseFuncTest {
    @Test
    @Story("Учет штрафов")
    @Description("Проверяет получение списка всех неоплаченных штрафов в системе")
    @DisplayName("GET /api/fines/unpaid — Все неоплаченные штрафы")
    public void testGetUnpaidFines() {
        given()
                .when()
                .get("/api/fines/unpaid")
                .then()
                .statusCode(200)
                .body("$", is(notNullValue()));
    }

    @Test
    @Story("Штрафы читателей")
    @Description("Проверяет получение списка всех штрафов конкретного читателя")
    @DisplayName("GET /api/fines/by-reader/{readerId} — Штрафы читателя")
    public void testGetFinesByReader() {
        given()
                .when()
                .get("/api/fines/by-reader/{readerId}", 1L)
                .then()
                .statusCode(200)
                .body("$", is(notNullValue()));
    }

    @Test
    @Story("Штрафы читателей")
    @Description("Проверяет получение общей суммы задолженности по неоплаченным штрафам читателя")
    @DisplayName("GET /api/fines/by-reader/{readerId}/sum — Сумма неоплаченных штрафов")
    public void testSumUnpaidByReader() {
        given()
                .when()
                .get("/api/fines/by-reader/{readerId}/sum", 1L)
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }

    @Test
    @Story("Оплата штрафов")
    @Description("Проверяет успешное изменение статуса штрафа на 'Оплачен'")
    @DisplayName("POST /api/fines/{fineId}/pay — Отметить штраф как оплаченный")
    public void testPayFine() {
        given()
                .when()
                .post("/api/fines/{fineId}/pay", 1L)
                .then()
                .statusCode(200)
                .body("id", equalTo(10));
    }
}
