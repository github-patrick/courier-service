package com.courier.service;

import com.courier.domain.CourierUser;
import com.courier.domain.DeliveryDriver;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.DeliveryDriverRequestDto;
import com.courier.domain.dtos.DeliveryDriverResponseDto;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.exception.CannotCreateDriverProfileException;
import com.courier.repository.DeliveryDriverRepository;
import com.courier.utils.DeliveryDriverUtils;
import com.courier.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Tag("delivery-driver")
class DeliveryDriverServiceImplTest {

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private DeliveryDriverRepository deliveryDriverRepository;

    @InjectMocks
    private DeliveryDriverServiceImpl deliverDriverService;


    @BeforeEach
    public void setUp() {
        deliverDriverService = new DeliveryDriverServiceImpl(deliveryDriverRepository,modelMapper);
    }

    @Test
    public void addDeliveryDriver() throws Exception {

        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto();
        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserDriverResponseDto();

        given(deliveryDriverRepository.save(Mockito.any(DeliveryDriver.class))).willReturn(new DeliveryDriver());


        DeliveryDriverResponseDto deliveryDriverResponseDto = deliverDriverService.addDeliveryDriver(deliveryDriverRequestDto, courierUserResponseDto);
        assertNotNull(deliveryDriverResponseDto);
    }


    @Test
    public void customerTypeUsersCannotAddDriverProfiles() {
        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto();
        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserCustomerResponseDto();

        assertThrows(CannotCreateDriverProfileException.class, ()-> deliverDriverService
                .addDeliveryDriver(deliveryDriverRequestDto, courierUserResponseDto));
    }

    @Test
    public void shouldNotHaveMoreThanOneDriverProfile() {
        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto();
        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserDriverResponseDto();

        given(deliveryDriverRepository.findByCourierUser(Mockito.any(CourierUser.class)))
                .willReturn(Optional.of(new DeliveryDriver()));

        assertThrows(CannotCreateDriverProfileException.class, ()->  deliverDriverService
                .addDeliveryDriver(deliveryDriverRequestDto, courierUserResponseDto));
    }

    @Test
    public void getAllDeliveryDrivers() {
        given(deliveryDriverRepository.findAll()).willReturn(Arrays.asList(new DeliveryDriver()));
        assertThat( deliverDriverService.getAllDeliveryDrivers().size(), equalTo(1));
    }


}