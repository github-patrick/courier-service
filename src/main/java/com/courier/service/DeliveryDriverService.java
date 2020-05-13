package com.courier.service;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.DeliveryDriverRequestDto;
import com.courier.domain.dtos.DeliveryDriverResponseDto;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.exception.CannotCreateDriverProfileException;

import java.util.List;

public interface DeliveryDriverService {

    DeliveryDriverResponseDto addDeliveryDriver(DeliveryDriverRequestDto deliveryDriverRequestDto,
                                                CourierUserResponseDto courierUserResponseDto) throws CannotCreateDriverProfileException;
    List<DeliveryDriverResponseDto> getAllDeliveryDrivers();

    List<DeliveryDriverResponseDto> getAllDeliveryDrivers(DeliveryDriverStatus status);

}
