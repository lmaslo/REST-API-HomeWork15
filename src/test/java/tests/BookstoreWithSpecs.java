package tests;

import io.qameta.allure.Description;
import models.lombok.CredentialsLombok;
import models.lombok.GenerateTokenResponseLombok;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static specs.Spesc.request;
import static specs.Spesc.responseSpec;

public class BookstoreWithSpecs {

    @Test
    @DisplayName("Запрос BookStore/v1/Books")
    @Description("Проверка, что запрос BookStore/v1/Books, возвращает статус 200 и количество книг больше чем 0")
    void getBooksTest() {
        given()
                .spec(request)
                .get("/BookStore/v1/Books")
                .then()
                .spec(responseSpec)
                .body("books", hasSize(greaterThan(0)))
                .body("books[0].isbn", equalTo("9781449325862"))
                .body("books.findAll{it}.isbn.flatten()", Matchers.hasItem("9781449325862"))
                .body("books.findAll{it.isbn=='9781449325862'}.title.flatten()", Matchers.hasItem("Git Pocket Guide"));
    }

    @Test
    @DisplayName("Запрос BookStore/v1/Book")
    @Description("Данные для отправки ISBN=9781449325862")
    void getBookTest() {

        String isbn = "9781449325862";
        given()
                .spec(request)
                .get("BookStore/v1/Book?ISBN={isbn}", isbn)
                .then()
                .spec(responseSpec)
                .body("isbn", is("9781449325862"));
    }

    @Test
    @DisplayName("Запрос Account/v1/GenerateToken")
    @Description("Проверка ответа на запрос. " +
            "Данные для отправки userName:alex, password: asdsad#frew_DFS2")
    void generateTokenWithLombokTest() {
        CredentialsLombok credentials = new CredentialsLombok();
        credentials.setUserName("alex");
        credentials.setPassword("asdsad#frew_DFS2");

        GenerateTokenResponseLombok tokenResponse =
                given()
                        .spec(request)
                        .body(credentials)
                        .when()
                        .post("Account/v1/GenerateToken")
                        .then()
                        .spec(responseSpec)
                        .extract().as(GenerateTokenResponseLombok.class);
    }
}
