package com.courier.utils;

import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.dtos.ParcelRequestDto;
import com.courier.domain.dtos.ParcelResponseDto;
import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import com.github.javafaker.Faker;

import java.util.Date;
import java.util.UUID;

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
}
