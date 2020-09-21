package com.courier.business;

import com.courier.domain.dtos.DeliveryDriverStatusDto;
import com.courier.exception.DeliveryDriverNotFoundException;

public interface DriverStatusResolver extends Resolvable<DeliveryDriverStatusDto> {

    @Override
    void updateStatus(DeliveryDriverStatusDto response, Long id) throws DeliveryDriverNotFoundException;
}
