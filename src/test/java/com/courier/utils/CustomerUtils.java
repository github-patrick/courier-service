package com.courier.utils;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerRequestDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.github.javafaker.Faker;

import java.util.Date;

public class CustomerUtils {
    private static Faker faker = Faker.instance();

    public static CustomerRequestDto getCustomerRequestDto() {
        return CustomerRequestDto.builder().fullName(faker.name().fullName()).build();
    }

    public static CustomerResponseDto getCustomerResponseDto(CourierUserResponseDto courierUserResponseDto) {
        return CustomerResponseDto.builder().id(1l).fullName(faker.name().fullName()).courierUser(courierUserResponseDto)
                .createdAt(new Date()).modifiedAt(new Date()).build();
    }
}
