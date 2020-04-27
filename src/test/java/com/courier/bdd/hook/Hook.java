package com.courier.bdd.hook;

import com.courier.CourierServiceApplication;
import com.courier.bdd.config.TestConfig;
import com.courier.config.AppConfig;
import com.courier.repository.CourierUserRepository;
import com.courier.repository.CustomerRepository;
import com.courier.repository.DeliveryDriverRepository;
import io.cucumber.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CourierServiceApplication.class, AppConfig.class, TestConfig.class})
@Slf4j
public class Hook {

    @Autowired
    private CourierUserRepository courierUserRepository;

    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Before
    public void setUp() {
        customerRepository.deleteAll();
        deliveryDriverRepository.deleteAll();
        courierUserRepository.deleteAll();
    }
}
