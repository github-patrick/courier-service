package com.courier.bdd;

import com.courier.domain.CourierUser;
import com.courier.domain.dtos.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Context {

    private CourierUserResponseDto courierUser;
    private DeliveryDriverResponseDto deliveryDriver;
    private CustomerResponseDto customer;
}
