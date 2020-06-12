package com.courier.repository;

import com.courier.domain.CourierUser;
import com.courier.domain.Customer;
import com.courier.domain.DeliveryDriver;
import com.courier.domain.Parcel;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import com.courier.domain.enums.UserType;
import com.courier.utils.CustomerUtils;
import com.courier.utils.ParcelUtils;
import com.courier.utils.UserUtils;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@EnableJpaAuditing
class DeliveryDriverRepositoryTest {

    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;

    @Autowired
    private CourierUserRepository courierUserRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ParcelRepository parcelRepository;

    @Test
    public void findDeliveryDriverByCourierUser() {
        CourierUser courierUser = this.persistAndGetCourierUser(UserType.DRIVER);
        this.persistAndGetDeliveryDriver(courierUser);

        DeliveryDriver deliveryDriver = deliveryDriverRepository.
                findByCourierUser(courierUserRepository.findByEmail(courierUser.getEmail()).get()).get();

        assertEquals(courierUser,deliveryDriver.getCourierUser());
    }

    @Test
    public void timeStampsShouldBeSet() {
        CourierUser courierUser = this.persistAndGetCourierUser(UserType.DRIVER);
        this.persistAndGetDeliveryDriver(courierUser);
        DeliveryDriver deliveryDriver = deliveryDriverRepository.
                findByCourierUser(courierUserRepository.findByEmail(courierUser.getEmail()).get()).get();

        assertNotNull(deliveryDriver.getCreatedAt());
        assertNotNull(deliveryDriver.getModifiedAt());
    }

    @Test
    public void shouldFilterDriversByStatus() {
        CourierUser courierUser = this.persistAndGetCourierUser(UserType.DRIVER);
        this.persistAndGetDeliveryDriver(courierUser);
        Iterable<DeliveryDriver> allByDeliveryDriverStatus =
                deliveryDriverRepository.findAllByDeliveryDriverStatus(DeliveryDriverStatus.UNAVAILABLE);

        List<DeliveryDriver> deliveryDriverList = new ArrayList();
        allByDeliveryDriverStatus.forEach(deliveryDriver -> deliveryDriverList.add(deliveryDriver));

        assertEquals(1, deliveryDriverList.size());
    }

    @Test
    public void shouldOrderDriversByCreatedAt() {
        for (int i = 0; i < 5; i++) {
            CourierUser courierUser = this.persistAndGetCourierUser(UserType.DRIVER);
            this.persistAndGetDeliveryDriver(courierUser);
        }

        deliveryDriverRepository.findAllByOrderByModifiedAtDesc().forEach(
                driver -> System.out.println(driver.getModifiedAt()));
    }

    @Test
    @Disabled("Review at a later date")
    public void shouldAssignParcelToDriver() {
        CourierUser courierUserDriver = this.persistAndGetCourierUser(UserType.DRIVER);
        CourierUser courierUserCustomer = this.persistAndGetCourierUser(UserType.CUSTOMER);
        DeliveryDriver deliveryDriver = this.persistAndGetDeliveryDriver(courierUserDriver);
        Customer customer = this.persistAndGetCustomer(courierUserCustomer);

        Parcel parcel = Parcel.builder().destination("London").origin("Rome").status(ParcelStatus.NOT_DISPATCHED)
                .sender(customer).priority(Priority.MEDIUM).build();

        parcelRepository.save(parcel);

        parcel.setDeliverer(deliveryDriver);

        parcelRepository.findAll();

        parcelRepository.save(parcel);

//        deliveryDriver.getParcels().add(parcel);
//        deliveryDriverRepository.save(deliveryDriver);

        assertEquals(1, deliveryDriver.getParcels().size());

    }

    private CourierUser persistAndGetCourierUser(UserType userType) {
         CourierUser courierUser = CourierUser.builder().email(new Faker().internet().emailAddress())
                .password("password").types(Arrays.asList(userType)).build();
         courierUserRepository.save(courierUser);
         return courierUser;
    }

    private DeliveryDriver persistAndGetDeliveryDriver(CourierUser courierUser) {
        DeliveryDriver deliveryDriver = DeliveryDriver.builder().fullName("Samantha Jennings")
                .deliveryDriverStatus(DeliveryDriverStatus.UNAVAILABLE)
                .parcels(new HashSet<>())
                .courierUser(courierUser).build();
        deliveryDriverRepository.save(deliveryDriver);

        return deliveryDriver;
    }

    private Customer persistAndGetCustomer(CourierUser courierUser) {
        Customer customer = Customer.builder().fullName("Kei Shinto").courierUser(courierUser)
                .build();
        customerRepository.save(customer);
        return customer;
    }
}