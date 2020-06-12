package com.courier.business;

import com.courier.domain.DeliveryDriver;
import com.courier.domain.Parcel;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.domain.enums.Priority;
import com.courier.repository.DeliveryDriverRepository;
import com.courier.repository.ParcelRepository;
import com.courier.service.DeliveryDriverService;
import com.courier.utils.DeliveryDriverUtils;
import com.courier.utils.ParcelUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParcelAssignerTest {

    private List<Parcel> parcels = new ArrayList();
    private List<DeliveryDriver> deliveryDrivers = new ArrayList();

    @Mock
    private ParcelRepository parcelRepository;

    @Mock
    private DeliveryDriverRepository deliveryDriverRepository;

    @Mock
    private DeliveryDriverService deliveryDriverService;

    @Mock
    private DriverSelectable driverSelectable;

    @Mock
    private ParcelSelectable parcelSelectable;


    @InjectMocks
    private ParcelAssignable parcelAssigner = new ParcelAssigner(parcelSelectable, driverSelectable, parcelRepository, deliveryDriverService);



    @BeforeEach
    void setUp() {
        parcels.add(ParcelUtils.getParcel(Priority.MEDIUM));
        parcels.add(ParcelUtils.getParcel(Priority.HIGH));
        parcels.add(ParcelUtils.getParcel(Priority.HIGH));
        parcels.add(ParcelUtils.getParcel(Priority.MEDIUM));
    }

    @Test
    void assignHighPriorityParcels() {
        when(driverSelectable.selectDriver()).thenReturn(Optional.of(DeliveryDriverUtils.getDeliveryDriver(DeliveryDriverStatus.AVAILABLE)));
        when(parcelSelectable.selectParcelHighPriority()).thenReturn(Optional.of(ParcelUtils.getParcel(Priority.HIGH)));
        parcelAssigner.assignHighPriorityParcelsToDriver();
    }

}