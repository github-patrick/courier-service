package com.courier.api.integration;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.enums.UserType;
import com.courier.utils.UserUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("dev") //dev profile uses a real database (non in memory of the JVM) to persist data.
public class BaseTest {

    protected static final String BASE_URI = "http://localhost";
    protected static final String BASE_PATH = "api/v1";
    protected static final String ADMIN_EMAIL = "admin@courier.com";

    protected RequestSpecBuilder requestSpecBuilder;
    protected ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter(LogDetail.BODY);

    public BaseTest() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = 8444;
        RestAssured.basePath = BASE_PATH;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        requestSpecBuilder = new RequestSpecBuilder();
        ResponseLoggingFilter.logResponseIfStatusCodeIs(201);
    }

    @BeforeAll
    public static void beforeAll() {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.ADMIN);
        courierUserRequestDto.setEmail(ADMIN_EMAIL);

        given()
                .contentType(ContentType.JSON)
                .body(courierUserRequestDto)
                .when()
                .post("courier-users")
                .then()
                .statusCode(201).extract().response();
    }

}
