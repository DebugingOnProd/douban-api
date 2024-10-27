package org.lhq.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lhq.entity.book.BookInfo;
import org.lhq.service.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class BookApiControllerTest {


    private static final Logger log = LoggerFactory.getLogger(BookApiControllerTest.class);

    @Test
    @DisplayName("get_book_by_id")
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
    @DisplayName("get_book_by_invalid_id")
    void getBook_InvalidId_ShouldReturnNull() {
        Response response = given().when().get("book/invalid");
        response.then().statusCode(200);
        BookInfo resultBody = response.as(BookInfo.class);
        log.info("请求结果:{}", resultBody);
        assertEquals(new BookInfo(), resultBody);
    }

    @Test
    @DisplayName("search_book_by_keyword")
    void searchBook_Return_Result() {
        Response response = given().when().get("book/search?keyword=三体");
        response.then().statusCode(200);
        String jsonStr = response.asString();
        List<BookInfo> bookInfoList = JsonUtils.readJsonToList(jsonStr, BookInfo.class);
        BookInfo actualBookInfo = bookInfoList.getFirst();
        InputStream resourceAsStream = getClass().getResourceAsStream("/books/search.json");
        BookInfo expectedBookInfo = JsonUtils.readInputStreamToJson(resourceAsStream, BookInfo.class);
        assertEquals(expectedBookInfo, actualBookInfo);
    }
}