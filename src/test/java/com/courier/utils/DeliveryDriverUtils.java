package com.courier.utils;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.DeliveryDriverRequestDto;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;

public class DeliveryDriverUtils {

    private static Faker faker = Faker.instance();

    public static DeliveryDriverRequestDto getDeliveryDriverRequestDto() {
        return DeliveryDriverRequestDto.builder().fullName(faker.name().fullName())
                .deliveryDriverStatus(DeliveryDriverStatus.UNAVAILABLE).build();
    }
}
