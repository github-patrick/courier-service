package com.courier.repository;

import com.courier.domain.Parcel;
import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
@EnableJpaAuditing
class ParcelRepositoryTest {

    @Autowired
    private ParcelRepository parcelRepository;

    @BeforeEach
    public void startUp() {
        this.addParcels();
    }

    @Test
    void findParcelByStatus() {
        List<Parcel> parcels = new ArrayList<>();
        Iterable<Parcel> parcelByPriority = parcelRepository.findParcelByStatus(ParcelStatus.NOT_DISPATCHED);
        parcelByPriority.forEach(parcel -> parcels.add(parcel));

        assertEquals(4, parcels.size());
    }

    @Test
    void findParcelByPriority() {
        List<Parcel> parcels = new ArrayList<>();
        Iterable<Parcel> parcelByPriority = parcelRepository.findParcelByPriority(Priority.LOW);
        parcelByPriority.forEach(parcel -> parcels.add(parcel));
        assertEquals(0, parcels.size());
    }

    @Test
    void findParcelByPriorityAndStatus() {
        List<Parcel> parcels = new ArrayList<>();
        Iterable<Parcel> parcelByPriority = parcelRepository.findParcelByStatusAndPriority(ParcelStatus.DELIVERED, Priority.HIGH);
        parcelByPriority.forEach(parcel -> parcels.add(parcel));
        assertEquals(1, parcels.size());
    }


    private void addParcels() {
        for (int i = 0; i < 4; i++) {
            Parcel parcel = Parcel.builder().id(UUID.randomUUID().toString()).destination("London").origin("Manchester")
                    .status(ParcelStatus.NOT_DISPATCHED)
                    .priority(Priority.MEDIUM).build();
            parcelRepository.save(parcel);
        }

        Parcel parcel = Parcel.builder().id(UUID.randomUUID().toString()).destination("London").origin("Manchester")
                .status(ParcelStatus.DELIVERED)
                .priority(Priority.HIGH).build();
        parcelRepository.save(parcel);
    }
}