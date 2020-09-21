package com.courier.service;

import com.courier.domain.CourierUser;
import com.courier.domain.Customer;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerRequestDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.enums.UserType;
import com.courier.exception.CannotCreateCustomerProfileException;
import com.courier.exception.CustomerNotFoundException;
import com.courier.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto, CourierUserResponseDto courierUserResponseDto)
            throws CannotCreateCustomerProfileException {

        log.info("Creating customer profile for user: {}", courierUserResponseDto);

        if (!courierUserResponseDto.getTypes().contains(UserType.CUSTOMER)) {
            throw new CannotCreateCustomerProfileException("User is not a customer. Register as a customer and retry.");
        }

        this.checkIfCustomerUserHasRegisteredProfile(courierUserResponseDto);

        customerRequestDto.setCourierUser(courierUserResponseDto);
        Customer customer = modelMapper.map(customerRequestDto, Customer.class);
        Customer customerSaved = customerRepository.save(customer);
        log.debug("Customer profile object persisted to the database {}",customerSaved);
        log.debug("Customer profile created.");
        return modelMapper.map(customerSaved, CustomerResponseDto.class);
    }

    public List<CustomerResponseDto> getAllCustomers() {
        List<CustomerResponseDto> customerResponseDtos = new ArrayList();
        customerRepository.findAll().forEach(customer ->
                customerResponseDtos.add(modelMapper.map(customer, CustomerResponseDto.class)));
        return customerResponseDtos;
    }

    @Override
    public CustomerResponseDto getCustomer(Long id) throws CustomerNotFoundException {
        Optional<Customer> customerFound = customerRepository.findById(id);
        if (customerFound.isPresent()) {
            return modelMapper.map(customerFound.get(),CustomerResponseDto.class);
        } else {
            throw new CustomerNotFoundException("customer with id " + id + " does not exist");
        }
    }

    private void checkIfCustomerUserHasRegisteredProfile(CourierUserResponseDto courierUserResponseDto)
            throws CannotCreateCustomerProfileException {
        log.info("Checking to see if the user has an existing customer profile.");
        if (customerRepository.findByCourierUser(modelMapper.map(courierUserResponseDto, CourierUser.class)).isPresent()) {
            log.debug("User {} already has a customer profile", courierUserResponseDto.getEmail());
            throw new CannotCreateCustomerProfileException("The user "+ courierUserResponseDto.getEmail() +" already has a customer profile");
        }
    }
}
