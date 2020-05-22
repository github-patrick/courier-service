package com.courier.controller;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.DeliveryDriverRequestDto;
import com.courier.domain.dtos.DeliveryDriverResponseDto;
import com.courier.domain.dtos.DeliveryDriverStatusDto;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.exception.CannotCreateDriverProfileException;
import com.courier.exception.CannotUpdateOtherUsersStatusException;
import com.courier.exception.CourierUserNotFoundException;
import com.courier.exception.DeliveryDriverNotFoundException;
import com.courier.service.CourierUserService;
import com.courier.service.DeliveryDriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

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

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DeliveryDriverResponseDto> getDriver(@PathVariable Long id) throws DeliveryDriverNotFoundException {
        DeliveryDriverResponseDto deliveryDriverResponseDto = deliveryDriverService.getDeliveryDriver(id);
        return new ResponseEntity(deliveryDriverResponseDto,HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<DeliveryDriverResponseDto>> retrieveAllDrivers(
            @RequestParam(required = false) String status) {

        EnumSet<DeliveryDriverStatus> deliveryDriverStatuses = EnumSet.allOf(DeliveryDriverStatus.class);
        Iterator<DeliveryDriverStatus> iterator = deliveryDriverStatuses.iterator();

        if (status != null) {
            while (iterator.hasNext()) {
                if (iterator.next().name().toUpperCase().equals(status.toUpperCase())) {
                    List<DeliveryDriverResponseDto> deliveryDriverResponseDtoList =
                            deliveryDriverService.getAllDeliveryDrivers(DeliveryDriverStatus.valueOf(status.toUpperCase()));
                    return new ResponseEntity(deliveryDriverResponseDtoList, HttpStatus.OK);
                }
            }
        }
        List<DeliveryDriverResponseDto> deliveryDriverResponseDtoList = deliveryDriverService.getAllDeliveryDrivers();
        return new ResponseEntity(deliveryDriverResponseDtoList, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_DRIVER"})
    @PatchMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateStatus(@RequestBody @Valid DeliveryDriverStatusDto deliveryDriverStatusDto,
                                          @PathVariable Long id, Principal principal, Authentication authentication)
            throws DeliveryDriverNotFoundException, CannotUpdateOtherUsersStatusException {

        if (authentication.getAuthorities().contains("ROLE_ADMIN")) {
            deliveryDriverService.updateStatus(deliveryDriverStatusDto, id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } if (!principal.getName().equals(deliveryDriverService.getDeliveryDriver(id).getCourierUser().getEmail())) {
            throw new CannotUpdateOtherUsersStatusException("You can only update your driver's status.");
        }

        deliveryDriverService.updateStatus(deliveryDriverStatusDto, id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
