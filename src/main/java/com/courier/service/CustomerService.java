package com.courier.service;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerRequestDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.exception.CannotCreateCustomerProfileException;

public interface CustomerService {

    CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto,
                                    CourierUserResponseDto courierUserResponseDto) throws CannotCreateCustomerProfileException;
}
