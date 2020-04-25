package com.courier.repository;

import com.courier.domain.CourierUser;
import com.courier.domain.DeliveryDriver;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = "classpath:sql/data-jpa-startup.sql")
class DeliveryDriverRepositoryTest {

    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;

    @Autowired
    private CourierUserRepository courierUserRepository;

    @BeforeEach
    public void setUp() {

    }


    @Test
    public void findDeliveryDriverByCourierUser() {
        CourierUser courierUser = courierUserRepository.save(CourierUser.builder().email("matthew.jones@gmail.com")
        .password("password").build());

        DeliveryDriver deliveryDriverToPersist = DeliveryDriver.builder().fullName("Samantha Jennings").deliveryDriverStatus(DeliveryDriverStatus.UNAVAILABLE)
                .courierUser(courierUser).build();

        deliveryDriverRepository.save(deliveryDriverToPersist);

        DeliveryDriver deliveryDriver = deliveryDriverRepository.
                findByCourierUser(courierUserRepository.findByEmail(courierUser.getEmail()).get()).get();

        assertEquals(courierUser,deliveryDriver.getCourierUser());
    }
}