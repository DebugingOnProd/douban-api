package org.lhq.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lhq.entity.book.BookInfo;
import org.lhq.service.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class BookApiControllerTest {


    private static final Logger log = LoggerFactory.getLogger(BookApiControllerTest.class);

    @Test
    @DisplayName("get book by id")
    void getBook_ValidId_ShouldReturnBookInfo() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/books/three_body.json");
        BookInfo bookInfo = JsonUtils.readInputStreamToJson(resourceAsStream, BookInfo.class);
        Response response = given().when().get("book/2567698");
        BookInfo resultBody = response.as(BookInfo.class);
        log.info("{}", resultBody);
        response.then().statusCode(200);
        assertEquals(bookInfo, resultBody);
    }
    @Test
    @DisplayName("get book by invalid id")
    void getBook_InvalidId_ShouldReturnNull() {
        Response response = given().when().get("book/invalid");
        response.then().statusCode(200);
        BookInfo resultBody = response.as(BookInfo.class);
        log.info("请求结果:{}", resultBody);
        assertEquals(new BookInfo(), resultBody);
    }
}