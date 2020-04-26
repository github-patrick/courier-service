package com.courier.repository;

import com.courier.config.AppConfig;
import com.courier.domain.CourierUser;
import com.courier.domain.DeliveryDriver;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.domain.enums.UserType;
import com.courier.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
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

    @Test
    public void findDeliveryDriverByCourierUser() {
        CourierUser courierUser = createCourierAndDriver();

        DeliveryDriver deliveryDriver = deliveryDriverRepository.
                findByCourierUser(courierUserRepository.findByEmail(courierUser.getEmail()).get()).get();

        assertEquals(courierUser,deliveryDriver.getCourierUser());
    }

    @Test
    public void timeStampsShouldBeSet() {
        CourierUser courierUser = createCourierAndDriver();
        DeliveryDriver deliveryDriver = deliveryDriverRepository.
                findByCourierUser(courierUserRepository.findByEmail(courierUser.getEmail()).get()).get();

        assertNotNull(deliveryDriver.getCreatedAt());
        assertNotNull(deliveryDriver.getModifiedAt());
    }


    private CourierUser createCourierAndDriver() {
        CourierUser courierUserToPersist = CourierUser.builder().email("matthew.jones@gmail.com")
                .userType(UserType.DRIVER).password("password").build();
        CourierUser courierUser = courierUserRepository.save(courierUserToPersist);

        DeliveryDriver deliveryDriverToPersist = DeliveryDriver.builder().fullName("Samantha Jennings").deliveryDriverStatus(DeliveryDriverStatus.UNAVAILABLE)
                .courierUser(courierUser).build();

        deliveryDriverRepository.save(deliveryDriverToPersist);
        return courierUser;
    }
}