package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.Credentials;
import models.GenerateTokenResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.Json;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static listeners.CustomAllureListener.withCustomTemplates;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;

public class BookstoreTests2 {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://demoqa.com";
    }

    @Test
    void getBookTest() {
        get("/BookStore/v1/Books")
                .then()
                .body("books", hasSize(greaterThan(0)))
                .statusCode(200);
    }

    @Test
    void getBookTestWithALLLogs() {
        given()
                .log().all()
                .when()
                .get("/BookStore/v1/Books")
                .then()
                .log().all()
                .body("books", hasSize(greaterThan(0)))
                .statusCode(200);
    }


    @Test
    void getBookTestWithSomeLogs() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .body("books", hasSize(greaterThan(0)))
                .statusCode(200);
    }


    @Test
    void generateTokenTest() {
        String data = "{ \"userName\": \"alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";

        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(data)
                .when()
                .post("Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("status", is("Success")).
                body("result", is("User authorized successfully."))
                .body("token.size()", (greaterThan(10)));
    }

    @Test
    void generateTokenWithAllureListenerTest() {
        String data = "{ \"userName\": \"alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";

        //   RestAssured.filters(new AllureRestAssured());
        given()
                .filter(new AllureRestAssured())
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(data)
                .when()
                .post("Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("status", is("Success")).
                body("result", is("User authorized successfully."))
                .body("token.size()", (greaterThan(10)));
    }


    @Test
    void generateTokenWithCustomAllureListenerTest() {
        String data = "{ \"userName\": \"alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";


        given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(data)
                .when()
                .post("Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("status", is("Success")).
                body("result", is("User authorized successfully."))
                .body("token.size()", (greaterThan(10)));
    }


    @Test
    void getTokenTest() {
        String data = "{ \"userName\": \"alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";

        String token =
                given()
                        .log().uri()
                        .log().body()
                        .contentType(JSON)
                        .body(data)
                        .when()
                        .post("Account/v1/GenerateToken")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .body("status", is("Success")).
                        body("result", is("User authorized successfully."))
                        .extract().path("token");

        System.out.println("Token: " + token);
    }

    @Test
    void generateTokenWithJsonSchemeCheckTest() {
        String data = "{ \"userName\": \"alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";


        given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(data)
                .when()
                .post("Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/GenerateToken_response_scheme.json"))
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", (greaterThan(10)));
    }

    @Test
    void generateTokenWithModelTest() {

        Credentials credentials = new Credentials();
        credentials.setUserName("alex");
        credentials.setPassword("asdsad#frew_DFS2");


        GenerateTokenResponse tokenResponse =
        given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(credentials)
                .when()
                .post("Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/GenerateToken_response_scheme.json"))
                .extract().as(GenerateTokenResponse.class);

        assertThat(tokenResponse.getStatus()).isEqualTo("Success");
        assertThat(tokenResponse.getResult()).isEqualTo("User authorized successfully.");
        assertThat(tokenResponse.getExpires()).hasSizeGreaterThan(10);
        assertThat(tokenResponse.getToken()).hasSizeGreaterThan(10).startsWith("eyJ");

    }


}
