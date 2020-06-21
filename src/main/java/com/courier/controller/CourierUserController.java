package com.courier.controller;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.exception.CannotRegisterUserException;
import com.courier.exception.CourierUserNotFoundException;
import com.courier.exception.ForbiddenActionException;
import com.courier.service.CourierUserService;
import com.courier.validators.CourierUserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("api/v1/courier-users")
public class CourierUserController {

    private CourierUserService courierUserService;

    @Autowired
    private CourierUserValidator courierUserValidator;

    @Autowired
    public CourierUserController(CourierUserService courierUserService) {
        this.courierUserService = courierUserService;
    }

    @InitBinder
    public void dataBind(WebDataBinder binder) {
        binder.addValidators(courierUserValidator);
    }

    @CrossOrigin(origins = {"http://localhost:8000"}, exposedHeaders = {"Location"})
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createCourierUser(@RequestBody @Valid CourierUserRequestDto courierUserRequestDto)
            throws CannotRegisterUserException {
        log.debug("Incoming courier user request body {}",courierUserRequestDto);

        CourierUserResponseDto courierUserResponseDto = courierUserService.addCourierUser(courierUserRequestDto);

        URI location =
                ServletUriComponentsBuilder.fromCurrentServletMapping().path("api/v1/courier-users/{id}").build()
                        .expand(courierUserResponseDto.getId()).toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity(courierUserResponseDto, headers, HttpStatus.CREATED);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, value = "{id}")
    public ResponseEntity<?> getCourierUser(@PathVariable Long id, Authentication authentication)
            throws CourierUserNotFoundException, ForbiddenActionException {
        CourierUserResponseDto courierUserResponseDto = courierUserService.getCourierUserById(id);

        if(!courierUserResponseDto.getEmail().equals(authentication.getName())) {
            throw new ForbiddenActionException("Cannot view this user. You must be logged with your own credentials");
        }
        return new ResponseEntity(courierUserResponseDto, HttpStatus.OK);
    }






}
