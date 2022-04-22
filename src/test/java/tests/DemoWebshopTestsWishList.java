package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class DemoWebshopTestsWishList {
    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
    }

    @Test
    void addToWishlishwithoutCookie() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_28_7_10=25" +
                        "&product_attribute_28_1_11=29" +
                        "&addtocart_28.EnteredQuantity=1")
                .when()
                .post("/addproducttocart/details/28/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", is("(1)"));
    }

    @Test
    void addToWishlishwithCookie() {
        String authCookie = "ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; __utmc=78382081; __utmz=78382081.1650105614.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utma=78382081.1414020670.1650105614.1650253243.1650287297.4; __utmt=1; nop.CompareProducts=CompareProductIds=13&CompareProductIds=72&CompareProductIds=31; __RequestVerificationToken=lPd7-gNRp65kdRi0-jHoWVF0f3byGyY2fWJSDqXO1eK44YonotVm5fvqKuoV8DOhdvz7H1Voxz69-lkLFRTpEZnEGQ0bJ6c76DjXk1PMW6w1; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=28&RecentlyViewedProductIds=27&RecentlyViewedProductIds=79&RecentlyViewedProductIds=44; ASP.NET_SessionId=bibuwyfyj4pgp1i4zzwbzv1l; Nop.customer=7312a57c-9048-45e6-b566-01637ba12a4f; __atuvc=7%7C15%2C13%7C16; __atuvs=625d62c539b96ba900b; __utmb=78382081.33.10.1650287297";
        String cartAsHtmlString = given()
                .contentType("application/x-www-form-urlencoded")
                .cookie(authCookie)
                .when()
                .get("http://demowebshop.tricentis.com")
                .then()
                .extract().asString();


        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(authCookie)
                .body("product_attribute_28_7_10=25" +
                        "&product_attribute_28_1_11=29" +
                        "&addtocart_28.EnteredQuantity=1")
                .when()
                .post("/addproducttocart/details/28/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"));
    }


    @Test
    @Disabled
    void addToWishlishwithAuth() {
        String authorizationCookie =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .formParam("Email", "qaguru@qa.guru")
                        .formParam("Password", "qaguru@qa.guru1")
                        .when()
                        .post("/login")
                        .then()
                        .statusCode(302)
                        .extract()
                        .cookie("NOPCOMMERCE.AUTH");

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");

        getWebDriver().manage().addCookie(
                new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(authorizationCookie)
                .body("product_attribute_28_7_10=25" +
                        "&product_attribute_28_1_11=29" +
                        "&addtocart_28.EnteredQuantity=1")
                .when()
                .post("/addproducttocart/details/28/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"));
    }


}
