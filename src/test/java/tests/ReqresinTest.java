package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresinTest {

    @Test
    void successfullLogin() {

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
                .body("token", is("QpwL5tke4Pnpja7X4"));


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
