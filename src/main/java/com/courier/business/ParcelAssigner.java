package com.courier.business;

import com.courier.domain.DeliveryDriver;
import com.courier.domain.Parcel;
import com.courier.domain.dtos.DeliveryDriverStatusDto;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.domain.enums.Priority;
import com.courier.exception.DeliveryDriverNotFoundException;
import com.courier.repository.DeliveryDriverRepository;
import com.courier.repository.ParcelRepository;
import com.courier.service.DeliveryDriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
@Service
public class ParcelAssigner implements ParcelAssignable {

    private ParcelSelectable parcelSelectable;
    private DriverSelectable driverSelectable;
    private ParcelRepository parcelRepository;
    private DeliveryDriverService deliveryDriverService;

    public ParcelAssigner(ParcelSelectable parcelSelectable, DriverSelectable driverSelectable, ParcelRepository parcelRepository, DeliveryDriverService deliveryDriverService) {
        this.parcelSelectable = parcelSelectable;
        this.driverSelectable = driverSelectable;
        this.parcelRepository = parcelRepository;
        this.deliveryDriverService = deliveryDriverService;
    }

    @Scheduled(fixedRate = 20000)
    @Override
    public void assignHighPriorityParcelsToDriver()  {
        Optional<Parcel> parcel = parcelSelectable.selectParcelHighPriority();
        DeliveryDriver deliveryDriver = this.getDeliveryDriverAndCheckParcelCount();
        Parcel parcelRetrieved;

        if (parcel.isPresent()) {
            parcelRetrieved = parcel.get();

            if (deliveryDriver != null) {
                parcelRetrieved.setDeliverer(deliveryDriver);
                parcelRepository.save(parcelRetrieved);
                log.info("Parcel: {} has been assigned to driver {} of id: {}",parcelRetrieved.getId(), deliveryDriver.getFullName(),
                        deliveryDriver.getId());
                parcelSelectable.removeFromHighPriorityQueue(parcelRetrieved);
            }
        }
    }

    @Scheduled(fixedRate = 30000)
    @Override
    public void assignMediumPriorityParcelsToDriver() {
        Optional<Parcel> parcel = parcelSelectable.selectParcelMediumPriority();
        DeliveryDriver deliveryDriver = this.getDeliveryDriverAndCheckParcelCount();
        Parcel parcelRetrieved;

        if (parcel.isPresent()) {
            parcelRetrieved = parcel.get();

            if (deliveryDriver != null) {
                parcelRetrieved.setDeliverer(deliveryDriver);
                parcelRepository.save(parcelRetrieved);
                log.info("Parcel: {} has been assigned to driver {} of id: {}",parcelRetrieved.getId(), deliveryDriver.getFullName(),
                        deliveryDriver.getId());
                parcelSelectable.removeFromMediumPriorityQueue(parcelRetrieved);
            }
        }

    }

    @Scheduled(fixedRate = 40000)
    @Override
    public void assignLowPriorityParcelsToDriver() {
        Optional<Parcel> parcel = parcelSelectable.selectParcelLowPriority();
        DeliveryDriver deliveryDriver = this.getDeliveryDriverAndCheckParcelCount();
        Parcel parcelRetrieved;

        if (parcel.isPresent()) {
            parcelRetrieved = parcel.get();

            if (deliveryDriver != null) {
                parcelRetrieved.setDeliverer(deliveryDriver);
                parcelRepository.save(parcelRetrieved);
                log.info("Parcel: {} has been assigned to driver {} of id: {}",parcelRetrieved.getId(), deliveryDriver.getFullName(),
                        deliveryDriver.getId());
                parcelSelectable.removeFromLowPriorityQueue(parcelRetrieved);
            }
        }
    }


    private DeliveryDriver getDeliveryDriverAndCheckParcelCount() {
        Optional<DeliveryDriver> deliveryDriver = driverSelectable.selectDriver();
        DeliveryDriver deliveryDriverRetrieved = null;

        if (deliveryDriver.isPresent()) {
            deliveryDriverRetrieved = deliveryDriver.get();
            if (deliveryDriverRetrieved.getParcels().size() == 3) {
                try {
                    deliveryDriverService.updateStatus(DeliveryDriverStatusDto.builder()
                            .deliveryDriverStatus(DeliveryDriverStatus.MAX_CAPACITY).build(), deliveryDriverRetrieved.getId());
                } catch (DeliveryDriverNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    deliveryDriverRetrieved = null;
                }
            } else {
                log.info("Delivery driver with id {} available for parcel delivery", deliveryDriverRetrieved.getId());
            }
        }
        return deliveryDriverRetrieved;
    }
}
