package com.courier.bdd;

import com.courier.domain.CourierUser;
import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.DeliveryDriverRequestDto;
import com.courier.domain.dtos.DeliveryDriverResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Context {

    private CourierUserResponseDto courierUser;
    private DeliveryDriverResponseDto deliveryDriver;
}
