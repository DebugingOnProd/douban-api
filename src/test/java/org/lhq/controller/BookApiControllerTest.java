package org.lhq.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lhq.entity.book.BookInfo;
import org.lhq.entity.book.BookVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class BookApiControllerTest {


    private static final Logger log = LoggerFactory.getLogger(BookApiControllerTest.class);

    @Test
    @DisplayName("获取图书信息")
    void getBook() {
        Response response = given().when().get("book/2567698");
        BookInfo resultBody = response.as(BookInfo.class);
        response.then().statusCode(200);
        log.info("{}", response.getBody().prettyPrint());
        String title = resultBody.getTitle();
        assertEquals("三体", title);
    }

    @Test
    @DisplayName("搜索图书")
    void searchBook() {
        Response response = given().when().queryParam("keyword", "三体").get("book/search");
        response.then().statusCode(200);
        BookInfo[] bookInfos = response.as(BookInfo[].class);
        List<BookInfo> bookInfoList = Arrays.asList(bookInfos);
        log.info("{}", response.getBody().prettyPrint());
        bookInfoList.stream().findFirst().ifPresent(item -> assertEquals("三体", item.getTitle()));
    }


    @Test
    @DisplayName("获取出版社列表")
    void getPublisher() {
        Response response = given().when().get("book/publisher");
        response.then().statusCode(200);
        log.info("{}", response.getBody().prettyPrint());
    }

    @Test
    @DisplayName("获取本地图书信息")
    void getLocalBook() {
        Response response = given().when().get("book/local/三体");
        response.then().statusCode(200);
        BookVo[] bookVos = response.as(BookVo[].class);
        log.info("{}", response.getBody().prettyPrint());
        Arrays.stream(bookVos)
                .findFirst()
                .ifPresent(
                        item -> assertEquals("三体全集", item.getTitle())
                );
    }

    @Test
    @DisplayName("获取本地图书")
    void getLocalBookTest() {
        Response response = given().when().get("/book/local/book/7163250");
        response.then().statusCode(200);
        log.info("{}", response.getBody().prettyPrint());
        BookInfo resultBody = response.as(BookInfo.class);
        assertEquals("明朝那些事儿", resultBody.getTitle());
    }




}