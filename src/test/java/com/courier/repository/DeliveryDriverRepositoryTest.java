package com.courier.repository;

import com.courier.domain.CourierUser;
import com.courier.domain.DeliveryDriver;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.domain.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@EnableJpaAuditing
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
                .types(Arrays.asList(UserType.DRIVER)).password("password").build();
        CourierUser courierUser = courierUserRepository.save(courierUserToPersist);

        DeliveryDriver deliveryDriverToPersist = DeliveryDriver.builder().fullName("Samantha Jennings")
                .deliveryDriverStatus(DeliveryDriverStatus.UNAVAILABLE)
                .courierUser(courierUser).build();

        deliveryDriverRepository.save(deliveryDriverToPersist);
        return courierUser;
    }
}