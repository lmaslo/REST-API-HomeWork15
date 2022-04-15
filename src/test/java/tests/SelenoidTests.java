package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {
    //make request to https://selenoid.autotests.cloud/status
    //total is 20

    @Test
    void checkTotal() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotalwithoutGiven() {

        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkBrowserVersionChrome() {

        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("browsers.chrome", hasKey("100.0"));
    }

    @Test
    void checkTotalBadPractice() {
        String response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response().asString();

        System.out.println("Response: " + response);

        String expectedResponse = "{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0," +
                "\"browsers\":" +
                "{\"chrome\":{\"100.0\":{},\"99.0\":{}}," +
                "\"firefox\":{\"97.0\":{},\"98.0\":{}}," +
                "\"opera\":{\"84.0\":{},\"85.0\":{}}}}\n";
        assertEquals(expectedResponse, response);
    }

    @Test
    void checkTotalGoodPractice() {
        int response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract()
                .path("total");


        System.out.println("Response: " + response);

        int expectedResponse = 20;
        assertEquals(expectedResponse, response);
    }

    @Test
    void responseExamples() {
        Response response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response();

        System.out.println("Response: " + response);
        System.out.println("Response .toString: " + response.toString());
        System.out.println("Response .asString: " + response.asString());
        System.out.println("Response: .path(\"total\") " + response.path("total"));
        System.out.println("Response: .path(\"browser.chrome\")" + response.path("browser.chrome"));
    }


    @Test
    void checkStatus401() {
        get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(401);
    }

    @Test
    void checkStatus200() {
        get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200);
    }

    @Test
    void checkStatus200WithAuth() {
        given()
                .auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200);
    }

}
