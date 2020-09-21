package com.courier.business;

import com.courier.domain.Parcel;

public interface ParcelAssignable {

    void assignHighPriorityParcelsToDriver();
    void assignMediumPriorityParcelsToDriver();
    void assignLowPriorityParcelsToDriver();
}
