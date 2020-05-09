package com.courier.utils;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.enums.UserType;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.Arrays;
import java.util.Date;

import static io.restassured.RestAssured.given;

public class UserUtils {

    private static Faker faker = Faker.instance();
    public static final String TEST_DEFAULT_PASSWORD = "password";

    public static CourierUserRequestDto getUser(UserType userType) {
        return CourierUserRequestDto.builder().email(faker.internet().emailAddress())
                .password(TEST_DEFAULT_PASSWORD).types(Arrays.asList(userType)).build();
    }

    public static CourierUserResponseDto getUserResponseDto(UserType userType) {
        return CourierUserResponseDto.builder().id(1l).email("random@courier.com")
                .createdAt(new Date()).modifiedAt(new Date())
                .password(TEST_DEFAULT_PASSWORD).types(Arrays.asList(userType)).build();
    }

    public static CourierUserResponseDto getUserCustomerDriverResponse() {
        return CourierUserResponseDto.builder().id(1l).email("random@courier.com")
                .password(TEST_DEFAULT_PASSWORD)
                .createdAt(new Date()).modifiedAt(new Date())
                .types(Arrays.asList(UserType.CUSTOMER, UserType.DRIVER)).build();
    }

    public static CourierUserResponseDto getUserCustomerResponseDto() {
        return CourierUserResponseDto.builder().id(1l).email("random@courier.com")
                .createdAt(new Date()).modifiedAt(new Date())
                .password(TEST_DEFAULT_PASSWORD).types(Arrays.asList(UserType.CUSTOMER)).build();
    }

    public static CourierUserResponseDto getUserDriverResponseDto() {
        return CourierUserResponseDto.builder().id(1l).email("random@courier.com")
                .createdAt(new Date()).modifiedAt(new Date())
                .password(TEST_DEFAULT_PASSWORD).types(Arrays.asList(UserType.DRIVER)).build();
    }

    public static CourierUserResponseDto createCourierUser(UserType userType) {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(getUser(userType))
                .log().all()
        .when()
                .post("/courier-users")
        .then().log().all().extract().as(CourierUserResponseDto.class);
    }

}
