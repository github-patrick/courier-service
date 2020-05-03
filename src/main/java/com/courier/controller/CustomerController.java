package com.courier.controller;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerRequestDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.exception.CannotCreateCustomerProfileException;
import com.courier.exception.CourierUserNotFoundException;
import com.courier.service.CourierUserService;
import com.courier.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private CustomerService customerService;
    private CourierUserService courierUserService;

    @Autowired
    public CustomerController(CustomerService customerService, CourierUserService courierUserService) {
        this.customerService = customerService;
        this.courierUserService = courierUserService;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody @Valid CustomerRequestDto customerRequestDto)
            throws CannotCreateCustomerProfileException, CourierUserNotFoundException {

        CourierUserResponseDto courierUserResponseDto = courierUserService.getCourierUserByEmail(customerRequestDto.getEmail());
        return new ResponseEntity(customerService.addCustomer(customerRequestDto,courierUserResponseDto), HttpStatus.CREATED);
    }
}
