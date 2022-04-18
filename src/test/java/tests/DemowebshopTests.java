package tests;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import org.openqa.selenium.Cookie;


public class DemowebshopTests {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
    }

    @Test
    void addToCartAsNewUserTest() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_72_5_18=53" +
                        "&product_attribute_72_6_19=54" +
                        "&product_attribute_72_3_20=57" +
                        "&addtocart_72.EnteredQuantity=1")
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your " +
                        "<a href=\"/cart\">shopping cart</a>"))
                .body("updatetopcartsectionhtml", is("(1)"));
    }

    @Test
    void differentQuantityAddToCartAsNewUserTest() {
        String enteredQuantity = "4";
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_72_5_18=53" +
                        "&product_attribute_72_6_19=54" +
                        "&product_attribute_72_3_20=57" +
                        "&addtocart_72.EnteredQuantity=" + enteredQuantity)
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your " +
                        "<a href=\"/cart\">shopping cart</a>"))
                .body("updatetopcartsectionhtml", is("(" + enteredQuantity + ")"));
    }

    @Test
    void addToCartWithCookieTest() {
        String authCookie = "Nop.customer=16d5f2e3-1840-400f-9e7a-cf319a2733fd;";
        String cartAsHtmlString = given()
                .contentType("application/x-www-form-urlencoded")
                .cookie(authCookie)
                .when()
                .get("http://demowebshop.tricentis.com")
                .then()
                .extract().asString();

        // todo refactor with jsoup
        int oldQuantity = Integer.parseInt(cartAsHtmlString.substring(
                cartAsHtmlString.lastIndexOf("cart-qty") + 11,
                cartAsHtmlString.indexOf("span", cartAsHtmlString.lastIndexOf("cart-qty")) - 3));
        int expectedQuantity = oldQuantity + 1;

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(authCookie)
                .body("product_attribute_72_5_18=53" +
                        "&product_attribute_72_6_19=54" +
                        "&product_attribute_72_3_20=57" +
                        "&addtocart_72.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your " +
                        "<a href=\"/cart\">shopping cart</a>"))
                .body("updatetopcartsectionhtml", is("(" + expectedQuantity + ")"));
    }

}


