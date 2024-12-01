package org.lhq.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lhq.entity.CelebrityInfo;
import org.lhq.entity.MovieInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class MovieApiControllerTest {

    private static final Logger log = LoggerFactory.getLogger(MovieApiControllerTest.class);


    @Test
    @DisplayName("Search_Movie_Test")
    void searchMovieTest() {
        Response response = given()
                .when()
                .queryParam("keyword", "流浪地球")
                .get("movie/search");
        response.then().statusCode(200);
        MovieInfo[] movieInfos = response.as(MovieInfo[].class);
        List<MovieInfo> list = Arrays.asList(movieInfos);
        log.info("movieInfos:{}", list);
        list.stream().findFirst().ifPresent(movieInfo -> {
            assertEquals("流浪地球2", movieInfo.getTitle());
        });
    }


    @Test
    @DisplayName("Get_Movie_Info_Test")
    void getMovieInfoTest() {
        Response response = given()
                .when()
                .pathParam("id", "35267208")
                .get("movie/{id}");
        response.then().statusCode(200);
        MovieInfo movieInfo = response.as(MovieInfo.class);
        log.info("movieInfo:{}", movieInfo);
        assertEquals("流浪地球2", movieInfo.getTitle());
    }


    @Test
    @DisplayName("Get_Movie_Cast_Test")
    void getMovieCastTest() {
        Response response = given()
                .when()
                .pathParam("id", "35267208")
                .get("movie/{id}/cast");
        response.then().statusCode(200);
        CelebrityInfo[] casts = response.as(CelebrityInfo[].class);
        List<CelebrityInfo> list = Arrays.asList(casts);
        log.info("list:{}", list);
        list.stream().findFirst().ifPresent(celebrityInfo -> {
            assertEquals("郭帆 Frant Gwo", celebrityInfo.getName());
        });
    }

}
