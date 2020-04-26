package com.courier.business;

import com.courier.domain.dtos.DeliveryDriverRequestDto;

public interface DriverStatusResolver extends Resolvable<DeliveryDriverRequestDto> {

    @Override
    void updateStatus(DeliveryDriverRequestDto response, Long id) throws Exception;
}
