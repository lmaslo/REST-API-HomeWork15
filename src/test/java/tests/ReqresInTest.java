package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresInTest {

    @Test
    void successfulRegister() {

        String authorizedData = "{\"email\": \"eve.holt@reqres.in\", " +
                "\"password\": \"pistol\"}";
        given()
                .body(authorizedData)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .body("token", is(notNullValue()))
                .body("id", is(4));

    }

    @Test
    void missingPasswordRegister() {

        String authorizedData = "{\"email\": \"eve.holt@reqres.in\"}";
        given()
                .body(authorizedData)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));

    }

    @Test
    void checkStatus404() {
        get("https://reqres.in/api/unknown/23")
                .then()
                .statusCode(404);
    }

    @Test
    void checkUserInfo() {
        given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));

    }

    @Test
    void checkList() {
        given()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .body("page", is(1))
                .body("total", is(12));


    }


    @Test
    void successfulLogin() {
    /*
    request https://reqres.in/api/login

    data:
    {
    "email": "eve.holt@reqres.in",
    "password": "cityslicka"
    }

    response:
    {
    "token": "QpwL5tke4Pnpja7X4"
    }

     */
        String authorizedData = "{\"email\": \"eve.holt@reqres.in\", " +
                "\"password\": \"cityslicka\"}";

        given()
                .body(authorizedData)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .body("token", is(notNullValue()));


    }


    @Test
    void missingPasswordLogin() {
    /*
    request https://reqres.in/api/login
    data:
    {
    "email": "eve.holt@reqres.in",
    }
    response:
    {
    "error": "Missing password"
    }
     */

        String authorizedData = "{\"email\": \"eve.holt@reqres.in\"}";

        given()
                .body(authorizedData)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));

    }


}
