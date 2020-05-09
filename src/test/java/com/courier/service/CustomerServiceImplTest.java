package com.courier.service;

import com.courier.domain.CourierUser;
import com.courier.domain.Customer;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerRequestDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.exception.CannotCreateCustomerProfileException;
import com.courier.repository.CustomerRepository;
import com.courier.utils.CustomerUtils;
import com.courier.utils.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    public void setUp() {
        customerService = new CustomerServiceImpl(customerRepository,modelMapper);
    }

    @Test
    void getAllCustomers() {
        given(customerRepository.findAll()).willReturn(Arrays.asList(new Customer()));
        List<CustomerResponseDto> customers = customerService.getAllCustomers();
        assertEquals(1, customers.size());
    }

    @Test
    public void driverTypeUsersCannotAddCustomerProfiles() {
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserDriverResponseDto();
        assertThrows(CannotCreateCustomerProfileException.class, ()-> customerService.addCustomer(customerRequestDto,courierUserResponseDto));

    }

    @Test
    public void driverAndCustomerTypeUserCreatesCustomer(){

        given(customerRepository.save(any(Customer.class))).willReturn(new Customer());

        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserCustomerDriverResponse();

        assertDoesNotThrow(()->customerService.addCustomer(customerRequestDto,courierUserResponseDto));
    }

    @Test
    public void shouldNotHaveMoreThanOneCustomerProfile() {

        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserCustomerResponseDto();

        given(customerRepository
                .findByCourierUser(Mockito.any(CourierUser.class)))
                .willReturn(Optional.of(new Customer()));

        assertThrows(CannotCreateCustomerProfileException.class, ()-> customerService.addCustomer(customerRequestDto,courierUserResponseDto));
    }
}