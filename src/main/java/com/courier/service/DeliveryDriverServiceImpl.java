package com.courier.service;

import com.courier.business.DriverStatusResolver;
import com.courier.domain.CourierUser;
import com.courier.domain.DeliveryDriver;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.DeliveryDriverRequestDto;
import com.courier.domain.dtos.DeliveryDriverResponseDto;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.domain.enums.UserType;
import com.courier.exception.CannotCreateDriverProfileException;
import com.courier.exception.DeliveryDriverNotFoundException;
import com.courier.repository.DeliveryDriverRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryDriverServiceImpl implements DeliveryDriverService, DriverStatusResolver {

    private DeliveryDriverRepository deliveryDriverRepository;
    private ModelMapper modelMapper;

    @Autowired
    public DeliveryDriverServiceImpl(DeliveryDriverRepository deliveryDriverRepository, ModelMapper modelMapper) {
        this.deliveryDriverRepository = deliveryDriverRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DeliveryDriverResponseDto addDeliveryDriver(DeliveryDriverRequestDto deliveryDriverRequestDto,
                                                       CourierUserResponseDto courierUserResponseDto) throws CannotCreateDriverProfileException {

        if (!courierUserResponseDto.getTypes().contains(UserType.DRIVER)) {
            throw new CannotCreateDriverProfileException("User is not a driver. Register as a driver and retry.");
        }

        if (!(DeliveryDriverStatus.UNAVAILABLE).equals(deliveryDriverRequestDto.getDeliveryDriverStatus())) {
            deliveryDriverRequestDto.setDeliveryDriverStatus(DeliveryDriverStatus.UNAVAILABLE);
        }

        checkIfDriverUserHasRegisteredProfile(courierUserResponseDto);

        deliveryDriverRequestDto.setCourierUser(courierUserResponseDto);
        DeliveryDriver deliveryDriver = modelMapper.map(deliveryDriverRequestDto, DeliveryDriver.class);

        DeliveryDriver savedDeliveryDriver = deliveryDriverRepository.save(deliveryDriver);
        return modelMapper.map(savedDeliveryDriver, DeliveryDriverResponseDto.class);
    }

    @Override
    public List<DeliveryDriverResponseDto> getAllDeliveryDrivers(DeliveryDriverStatus status) {
        List<DeliveryDriverResponseDto> deliveryDriverResponseDtoList = new ArrayList();
        deliveryDriverRepository.findByDeliveryDriverStatus(status).forEach(deliveryDriver ->
                deliveryDriverResponseDtoList.add(modelMapper.map(deliveryDriver,DeliveryDriverResponseDto.class)));
        return deliveryDriverResponseDtoList;
    }

    @Override
    public List<DeliveryDriverResponseDto> getAllDeliveryDrivers() {
        List<DeliveryDriverResponseDto> deliveryDriverResponseDtoList = new ArrayList();
        deliveryDriverRepository.findAll().forEach(deliveryDriver ->
                deliveryDriverResponseDtoList.add(modelMapper.map(deliveryDriver,DeliveryDriverResponseDto.class)));
        return deliveryDriverResponseDtoList;
    }

    private void checkIfDriverUserHasRegisteredProfile(CourierUserResponseDto courierUserResponseDto) throws CannotCreateDriverProfileException {
        if (deliveryDriverRepository.findByCourierUser(modelMapper.map(courierUserResponseDto,CourierUser.class)).isPresent())
        {
            throw new CannotCreateDriverProfileException("The user "+ courierUserResponseDto.getEmail() +" already has a driver profile");
        }
    }

    public DeliveryDriverResponseDto getDeliveryDriver(Long id) throws DeliveryDriverNotFoundException {
        return modelMapper.map(
                deliveryDriverRepository.findById(id).orElseThrow(DeliveryDriverNotFoundException::new), DeliveryDriverResponseDto.class);
    }

    @Override
    public void updateStatus(DeliveryDriverRequestDto deliveryDriverRequestDto, Long driverId) throws DeliveryDriverNotFoundException {
        DeliveryDriverResponseDto driverResponseDto = getDeliveryDriver(driverId);

        driverResponseDto.setDeliveryDriverStatus(deliveryDriverRequestDto.getDeliveryDriverStatus());
        deliveryDriverRepository.save(modelMapper.map(driverResponseDto, DeliveryDriver.class));
    }
}
