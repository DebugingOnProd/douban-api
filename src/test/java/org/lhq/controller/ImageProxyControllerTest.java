package org.lhq.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ImageProxyControllerTest {
    @Test
    void testGetImage() {
        given()
        .when().get("/imageProxy/view/cover?cover=https://img9.doubanio.com/view/personage/m/public/6ac5b0a114e0f12f1d3ef9ab9ab17185.jpg")
        .then().assertThat().statusCode(200);
    }

}
