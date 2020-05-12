package com.courier.controller;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.DeliveryDriverRequestDto;
import com.courier.domain.dtos.DeliveryDriverResponseDto;
import com.courier.exception.CannotCreateDriverProfileException;
import com.courier.exception.CourierUserNotFoundException;
import com.courier.service.CourierUserService;
import com.courier.service.DeliveryDriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/drivers")
public class DeliveryDriverController {

    private DeliveryDriverService deliveryDriverService;
    private CourierUserService courierUserService;

    @Autowired
    public DeliveryDriverController(DeliveryDriverService deliveryDriverService, CourierUserService courierUserService) {
        this.deliveryDriverService = deliveryDriverService;
        this.courierUserService = courierUserService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DeliveryDriverResponseDto> createDriver(@RequestBody @Valid DeliveryDriverRequestDto deliveryDriverRequestDto)
            throws CourierUserNotFoundException, CannotCreateDriverProfileException {

        CourierUserResponseDto courierUserResponseDto = courierUserService.getCourierUserByEmail(deliveryDriverRequestDto.getEmail());
        DeliveryDriverResponseDto deliveryDriverResponseDto = deliveryDriverService.addDeliveryDriver(deliveryDriverRequestDto, courierUserResponseDto);
        return new ResponseEntity(deliveryDriverResponseDto, HttpStatus.CREATED);
    }
}
