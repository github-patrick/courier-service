package com.courier.repository;

import com.courier.domain.Parcel;
import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParcelRepository extends CrudRepository<Parcel, String> {

    Iterable<Parcel> findParcelByStatus(ParcelStatus parcelStatus);
    Iterable<Parcel> findParcelByPriority(Priority priority);
    Iterable<Parcel> findParcelByStatusAndPriority(ParcelStatus parcelStatus, Priority priority);
}
