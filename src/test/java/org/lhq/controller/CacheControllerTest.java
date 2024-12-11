package org.lhq.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CacheControllerTest {
    @Test
    @DisplayName("test_cache_size")
    void testSize() {
        given()
                .when().get("/cache/size")
                .then()
                .statusCode(200);
    }
}
