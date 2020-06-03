package com.courier.utils;

import com.courier.domain.dtos.*;
import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import com.courier.domain.enums.UserType;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.Date;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class ParcelUtils {

    private static Faker faker = Faker.instance();

    public static ParcelRequestDto getParcelRequestDto(Priority priority) {
        return ParcelRequestDto.builder().origin(faker.address().cityName()).destination(faker.address().cityName()).priority(priority).build();
    }

    public static ParcelRequestDto getParcelRequestDto() {
        return ParcelRequestDto.builder().origin(faker.address().cityName()).destination(faker.address().cityName()).build();
    }

    public static ParcelResponseDto getParcelResponseDto(CustomerResponseDto customerResponseDto) {
        return ParcelResponseDto.builder().id(UUID.randomUUID().toString())
                .destination(faker.address().cityName())
                .origin(faker.address().cityName())
                .status(ParcelStatus.NOT_DISPATCHED)
                .createdAt(new Date()).modifiedAt(new Date())
                .sender(customerResponseDto)
                .priority(Priority.MEDIUM).build();
    }

    public static ParcelResponseDto createParcel(CustomerResponseDto customerResponseDto,
                                                 CourierUserResponseDto courierUserResponseDto) {

        ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto();

        ParcelResponseDto parcelResponseDto = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .auth().preemptive().basic(courierUserResponseDto.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)
                .body(parcelRequestDto)
                .log().all()
                .pathParam("id", customerResponseDto.getId())
                .when()
                .post("customers/{id}/parcels")
                .then()
                .log().all()
                .statusCode(201)
                .extract().as(ParcelResponseDto.class);

        return parcelResponseDto;
    }

}
