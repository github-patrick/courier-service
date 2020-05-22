package com.courier.business;

import com.courier.domain.dtos.DeliveryDriverRequestDto;

@FunctionalInterface
public interface Resolvable<T> {

    void updateStatus(T body, Long id) throws Exception;
}
