package com.courier.utils;

import com.courier.domain.CourierUser;
import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.enums.UserType;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class UserUtils {

    private static Faker faker = Faker.instance();
    public static final String TEST_DEFAULT_PASSWORD = "password";

    public static CourierUserRequestDto getUser(UserType userType) {
        return CourierUserRequestDto.builder().email(faker.internet().emailAddress())
                .password(TEST_DEFAULT_PASSWORD).userType(userType).build();
    }

    public static CourierUserResponseDto getUserResponseDto(UserType userType) {
        return CourierUserResponseDto.builder().id(1l).email("random@courier.com")
                .password(TEST_DEFAULT_PASSWORD).userType(userType).build();
    }

    public static CourierUserResponseDto getUserCustomerResponseDto() {
        return CourierUserResponseDto.builder().id(1l).email("random@courier.com")
                .password(TEST_DEFAULT_PASSWORD).userType(UserType.CUSTOMER).build();
    }

    public static CourierUserResponseDto getUserDriverResponseDto() {
        return CourierUserResponseDto.builder().id(1l).email("random@courier.com")
                .password(TEST_DEFAULT_PASSWORD).userType(UserType.DRIVER).build();
    }

    public static CourierUserResponseDto createCourierUser(UserType userType) {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(getUser(userType))
        .when()
                .post("/courier-users")
        .then().extract().as(CourierUserResponseDto.class);
    }

}
