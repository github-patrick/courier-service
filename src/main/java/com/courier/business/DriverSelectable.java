package com.courier.business;

import com.courier.domain.DeliveryDriver;

import java.util.Optional;

public interface DriverSelectable {

    Optional<DeliveryDriver> selectDriver();
}
