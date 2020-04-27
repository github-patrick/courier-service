package com.courier.repository;

import com.courier.domain.CourierUser;
import com.courier.domain.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer,Long> {
    Optional<Customer> findByCourierUser(CourierUser courierUser);
}
