package com.courier.repository;

import com.courier.domain.CourierUser;
import com.courier.domain.Customer;
import com.courier.domain.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CustomerRepositoryTest {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CourierUserRepository courierUserRepository;

    @Test
    public void findCustomerByUserTest() {

        CourierUser courierUser = createCourierAndCustomer();

        Customer customer = customerRepository
                .findByCourierUser(courierUserRepository.findByEmail(courierUser.getEmail()).get()).get();

        assertEquals(courierUser,customer.getCourierUser());
    }

    private CourierUser createCourierAndCustomer() {
        CourierUser courierUserToPersist = CourierUser.builder().email("matthew.jones@gmail.com")
                .userType(UserType.CUSTOMER).password("password").build();
        CourierUser courierUser = courierUserRepository.save(courierUserToPersist);

        Customer customer = Customer.builder().fullName("Matthew Berkow").courierUser(courierUser).build();

        customerRepository.save(customer);
        return courierUser;
    }
}
