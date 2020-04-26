package com.courier.repository;

import com.courier.domain.CourierUser;
import com.courier.domain.DeliveryDriver;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DeliveryDriverRepository  extends CrudRepository<DeliveryDriver, Long> {

    Optional<DeliveryDriver> findByCourierUser(CourierUser courierUser);
}