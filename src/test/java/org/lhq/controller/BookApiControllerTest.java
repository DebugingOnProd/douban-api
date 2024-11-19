package org.lhq.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.lhq.entity.book.BookInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class BookApiControllerTest {


    private static final Logger log = LoggerFactory.getLogger(BookApiControllerTest.class);

    @Test
    void getBook() {
        Response response = given().when().get("book/2567698");
        BookInfo resultBody = response.as(BookInfo.class);
        response.then().statusCode(200);
        log.info("{}", response.getBody().prettyPrint());
        String title = resultBody.getTitle();
        assertEquals("三体", title);
    }
}