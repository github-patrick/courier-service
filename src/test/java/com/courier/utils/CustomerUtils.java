package com.courier.utils;

import com.courier.domain.Customer;
import com.courier.domain.Parcel;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerRequestDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.enums.UserType;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;

import java.util.Date;

import static io.restassured.RestAssured.given;

public class CustomerUtils {
    private static Faker faker = Faker.instance();

    public static CustomerRequestDto getCustomerRequestDto() {
        return CustomerRequestDto.builder().fullName(faker.name().fullName()).build();
    }

    public static CustomerResponseDto getCustomerResponseDto(CourierUserResponseDto courierUserResponseDto) {
        return CustomerResponseDto.builder().id(1l).fullName(faker.name().fullName()).courierUser(courierUserResponseDto)
                .createdAt(new Date()).modifiedAt(new Date()).build();
    }

    public static CustomerResponseDto getCustomerResponseDto() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserCustomerResponseDto();
        return CustomerResponseDto.builder().id(1l).fullName(faker.name().fullName()).courierUser(courierUserResponseDto)
                .createdAt(new Date()).modifiedAt(new Date()).build();
    }

    public static Customer getCustomer() {
        return Customer.builder().fullName(faker.name().fullName()).id(1l).courierUser(UserUtils.getCourierUser(UserType.CUSTOMER))
                .build();
    }

    public static CustomerResponseDto createCustomer(CourierUserResponseDto courierUserResponseDto) {

        CustomerRequestDto customerRequestDto = getCustomerRequestDto();
        customerRequestDto.setEmail(courierUserResponseDto.getEmail());

        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .auth().preemptive().basic(courierUserResponseDto.getEmail(),UserUtils.TEST_DEFAULT_PASSWORD)
                .body(customerRequestDto).
                        log().all().
                        when()
                .post("/customers").
                        then()
                .log().all().extract().as(CustomerResponseDto.class);
    }
}
