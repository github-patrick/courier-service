package com.courier.utils;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.DeliveryDriverRequestDto;
import com.courier.domain.dtos.DeliveryDriverResponseDto;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static io.restassured.RestAssured.given;

public class DeliveryDriverUtils {

    private static Faker faker = Faker.instance();

    public static DeliveryDriverRequestDto getDeliveryDriverRequestDto() {
        return DeliveryDriverRequestDto.subBuilder().fullName(faker.name().fullName())
                .email(faker.internet().emailAddress()).build();
    }

    public static DeliveryDriverRequestDto getDeliveryDriverRequestDto(String email) {
        return DeliveryDriverRequestDto.subBuilder().fullName(faker.name().fullName())
                .email(email).build();
    }

    public static DeliveryDriverResponseDto getDeliveryDriverResponseDto(CourierUserResponseDto courierUserResponseDto) {
        return DeliveryDriverResponseDto.builder().id(1l).fullName(faker.name().fullName())
                .deliveryDriverStatus(DeliveryDriverStatus.UNAVAILABLE)
                .createdAt(new Date()).modifiedAt(new Date()).courierUser(courierUserResponseDto).build();
    }

    public static DeliveryDriverResponseDto createDeliveryDriver(CourierUserResponseDto courierUserResponseDto) {

        DeliveryDriverRequestDto deliveryDriverRequestDto = getDeliveryDriverRequestDto(courierUserResponseDto.getEmail());


        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .auth().preemptive().basic(courierUserResponseDto.getEmail(),UserUtils.TEST_DEFAULT_PASSWORD)
                .body(deliveryDriverRequestDto)
                .log().all()
        .when()
                .post("/drivers")
        .then()
                .log().all()
                .extract().as(DeliveryDriverResponseDto.class);
    }
}
