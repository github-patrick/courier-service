package com.courier.utils;

import com.courier.domain.dtos.CustomerRequestDto;
import com.github.javafaker.Faker;

public class CustomerUtils {
    private static Faker faker = Faker.instance();

    public static CustomerRequestDto getCustomerRequestDto() {
        return CustomerRequestDto.builder().fullName(faker.name().fullName()).build();
    }
}
