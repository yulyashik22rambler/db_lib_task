package org.example.library;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("Библиотечный сервис")
@Feature("Штрафы")
public class FineApiTest extends BaseFuncTest {
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
        ValidatableResponse body = given()
                .when()
                .get("/api/fines/unpaid")
                .then()
                .statusCode(200)
                .body("$", is(notNullValue()));

        ArrayList list = (ArrayList) body.extract().body().jsonPath().get();
        LinkedHashMap first = (LinkedHashMap) list.get(0);
        Integer recordId = (Integer) first.get("id");
        Long id = Long.valueOf(recordId);

        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .when()
                .post("/api/fines/{fineId}/pay", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(Integer.valueOf(String.valueOf(id))));
    }//in db set not paid and delete date
}
