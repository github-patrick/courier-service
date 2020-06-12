package com.courier.business;

import com.courier.domain.Parcel;

import java.util.Optional;

public interface ParcelSelectable {

    Optional<Parcel> selectParcelHighPriority();
    Optional<Parcel> selectParcelMediumPriority();
    Optional<Parcel> selectParcelLowPriority();

    boolean removeFromHighPriorityQueue(Parcel parcel);
    boolean removeFromMediumPriorityQueue(Parcel parcel);
    boolean removeFromLowPriorityQueue(Parcel parcel);
}
