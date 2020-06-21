package com.courier.business;

import com.courier.domain.DeliveryDriver;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.repository.DeliveryDriverRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class DriverSelector implements DriverSelectable {

    private DeliveryDriverRepository deliveryDriverRepository;

    @Autowired
    public DriverSelector(DeliveryDriverRepository deliveryDriverRepository) {
        this.deliveryDriverRepository = deliveryDriverRepository;
    }

    @Override
    public Optional<DeliveryDriver> selectDriver() {
        List<DeliveryDriver> driverList = new ArrayList();
        deliveryDriverRepository.findAllByOrderByModifiedAtDesc().forEach(deliveryDriver -> driverList.add(deliveryDriver));

        Optional<DeliveryDriver> deliveryDriverOptional = driverList.stream().filter(this.driverPredicate()).findFirst();
        if (!deliveryDriverOptional.isPresent()) {
            return Optional.empty();
        }
        return deliveryDriverOptional;
    }

    private Predicate<DeliveryDriver> driverPredicate() {
        return driver -> (driver.getDeliveryDriverStatus().equals(DeliveryDriverStatus.AVAILABLE));
    }
}
