package com.courier.controller;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.service.CourierUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/courier-users")
public class CourierUserController {

    private CourierUserService courierUserService;

    @Autowired
    public CourierUserController(CourierUserService courierUserService) {
        this.courierUserService = courierUserService;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CourierUserResponseDto> createCourierUser(@RequestBody @Valid CourierUserRequestDto courierUserRequestDto) {
        return new ResponseEntity(courierUserService.addCourierUser(courierUserRequestDto), HttpStatus.CREATED);
    }

}
