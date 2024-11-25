package org.lhq.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lhq.entity.MovieInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class MovieApiControllerTest {

    private static final Logger log = LoggerFactory.getLogger(BookApiControllerTest.class);


    @Test
    @DisplayName("Search_Movie_Test")
    void searchMovieTest() {
        Response response = given()
                .when()
                .queryParam("keyword", "流浪地球")
                .get("movie/search");
        response.then().statusCode(200);
        MovieInfo[] movieInfos = response.as(MovieInfo[].class);
        log.info("movieInfos:{}", movieInfos);
        List<MovieInfo> list = Arrays.asList(movieInfos);
        list.stream().findFirst().ifPresent(movieInfo -> {
            assertEquals("流浪地球2", movieInfo.getTitle());
        });
    }

}
