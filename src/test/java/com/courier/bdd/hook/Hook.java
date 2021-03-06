package com.courier.bdd.hook;

import com.courier.CourierServiceApplication;
import com.courier.bdd.config.TestConfig;
import com.courier.business.ParcelAssignable;
import com.courier.business.ParcelSelectable;
import com.courier.config.AppConfig;
import com.courier.config.SecurityConfig;
import com.courier.repository.CourierUserRepository;
import com.courier.repository.CustomerRepository;
import com.courier.repository.DeliveryDriverRepository;
import com.courier.repository.ParcelRepository;
import io.cucumber.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@ContextConfiguration(classes = {CourierServiceApplication.class, AppConfig.class, TestConfig.class})
@TestPropertySource("classpath:application-test.properties")
@Slf4j
public class Hook {

    @Autowired
    private CourierUserRepository courierUserRepository;

    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ParcelRepository parcelRepository;

    private ParcelSelectable parcelSelectable;

    @Before()
    public void setUp() {
        parcelRepository.deleteAll();
        customerRepository.deleteAll();
        deliveryDriverRepository.deleteAll();
        courierUserRepository.deleteAll();
    }
}
