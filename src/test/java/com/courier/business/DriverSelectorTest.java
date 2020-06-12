package com.courier.business;

import com.courier.domain.DeliveryDriver;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.repository.DeliveryDriverRepository;
import com.courier.utils.DeliveryDriverUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverSelectorTest {

    @Mock
    private DeliveryDriverRepository deliveryDriverRepository;

    @InjectMocks
    private DriverSelectable driverSelector = new DriverSelector(deliveryDriverRepository);

    private List<DeliveryDriver> deliveryDriverList;


    @BeforeEach
    public void init() {
        deliveryDriverList = Arrays.asList(
                DeliveryDriverUtils.getDeliveryDriver(DeliveryDriverStatus.ON_DELIVERY),
                DeliveryDriverUtils.getDeliveryDriver(DeliveryDriverStatus.UNAVAILABLE),
                DeliveryDriverUtils.getDeliveryDriver(DeliveryDriverStatus.AVAILABLE),
                DeliveryDriverUtils.getDeliveryDriver(DeliveryDriverStatus.MAX_CAPACITY));
    }

    @Test
    void selectDriver() {
        when(deliveryDriverRepository.findAllByOrderByModifiedAtDesc()).thenReturn(deliveryDriverList);
        DeliveryDriver deliveryDriver = driverSelector.selectDriver().get();
        assertEquals(DeliveryDriverStatus.AVAILABLE, deliveryDriver.getDeliveryDriverStatus());
    }

    @Test
    public void noDriverDeliveryFound() {
        when(deliveryDriverRepository.findAllByOrderByModifiedAtDesc()).thenReturn(new ArrayList());
        assertFalse(driverSelector.selectDriver().isPresent());
    }
}