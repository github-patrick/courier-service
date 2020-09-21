package com.courier.api.integration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
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
}
