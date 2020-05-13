package com.courier.repository;

import com.courier.domain.CourierUser;
import com.courier.domain.DeliveryDriver;
import com.courier.domain.enums.DeliveryDriverStatus;
import org.springframework.data.repository.CrudRepository;

import java.lang.ref.SoftReference;
import java.util.Optional;

public interface DeliveryDriverRepository  extends CrudRepository<DeliveryDriver, Long> {

    Optional<DeliveryDriver> findByCourierUser(CourierUser courierUser);

    Iterable<DeliveryDriver> findAllByDeliveryDriverStatus(DeliveryDriverStatus status);

    Iterable<DeliveryDriver> findByDeliveryDriverStatus(DeliveryDriverStatus status);


}