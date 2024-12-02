package org.lhq.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lhq.entity.PersonageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class PersonApiControllerTest {

    private static final Logger log = LoggerFactory.getLogger(PersonApiControllerTest.class);


    @Test
    @DisplayName("获取人物信息")
    void Get_Person_Test() {
        log.info("获取人物信息");
        Response response = given()
                .when()
                .get("person/27227726");
        response.then().statusCode(200);
        PersonageInfo personageInfo = response.as(PersonageInfo.class);
        assertNotNull(personageInfo);
        assertEquals("姜文 Wen Jiang", personageInfo.getName());
    }

}
