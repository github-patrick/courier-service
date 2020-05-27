package com.courier.repository;

import com.courier.domain.Parcel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParcelRepository extends CrudRepository<Parcel, UUID> {
}
