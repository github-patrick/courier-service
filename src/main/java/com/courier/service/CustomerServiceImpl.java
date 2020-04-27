package com.courier.service;

import com.courier.domain.CourierUser;
import com.courier.domain.Customer;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerRequestDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.enums.UserType;
import com.courier.exception.CannotCreateCustomerProfileException;
import com.courier.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto,
                                           CourierUserResponseDto courierUserResponseDto) throws CannotCreateCustomerProfileException {


        if (courierUserResponseDto.getUserType().equals(UserType.DRIVER)) {
            throw new CannotCreateCustomerProfileException();
        }

        checkIfCustomerUserHasRegisteredProfile(courierUserResponseDto);

        customerRequestDto.setCourierUser(courierUserResponseDto);
        Customer customer = modelMapper.map(customerRequestDto, Customer.class);
        Customer customerSaved = customerRepository.save(customer);
        return modelMapper.map(customerSaved, CustomerResponseDto.class);
    }


    public List<CustomerResponseDto> getAllCustomers() {
        List<CustomerResponseDto> customerResponseDtos = new ArrayList();
        customerRepository.findAll().forEach(customer ->
                customerResponseDtos.add(modelMapper.map(customer, CustomerResponseDto.class)));
        return customerResponseDtos;
    }

    private void checkIfCustomerUserHasRegisteredProfile(CourierUserResponseDto courierUserResponseDto) throws CannotCreateCustomerProfileException {
        if (customerRepository.findByCourierUser(modelMapper.map(courierUserResponseDto, CourierUser.class)).isPresent()) {
            throw new CannotCreateCustomerProfileException("The user "+ courierUserResponseDto.getEmail() +" already has a customer profile");
        }
    }
}
