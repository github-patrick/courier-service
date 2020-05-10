package com.courier.api.integration;

import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("dev")
public class BaseTest {

    protected static final String BASE_URI = "http://localhost";
    protected static final String BASE_PATH = "api/v1";

    public BaseTest() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = 8444;
        RestAssured.basePath = BASE_PATH;
    }
}
