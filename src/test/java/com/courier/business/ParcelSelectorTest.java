package com.courier.business;

import com.courier.domain.Parcel;
import com.courier.domain.enums.Priority;
import com.courier.repository.ParcelRepository;
import com.courier.utils.ParcelUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParcelSelectorTest {

    private List<Parcel> parcelHighPriorityList;
    private List<Parcel> parcelMediumPriorityList;
    private List<Parcel> parcelLowPriorityList;

    @Mock
    private ParcelRepository parcelRepository;

    @InjectMocks
    private ParcelSelectable parcelSelectable = new ParcelSelector(parcelRepository);

    @BeforeEach
    public void init() {
        parcelHighPriorityList = Arrays.asList(
                ParcelUtils.getParcel(Priority.HIGH),
                ParcelUtils.getParcel(Priority.HIGH)
        );
        parcelMediumPriorityList = Arrays.asList(
                ParcelUtils.getParcel(Priority.MEDIUM),
                ParcelUtils.getParcel(Priority.MEDIUM)
        );
        parcelLowPriorityList = Arrays.asList(
                ParcelUtils.getParcel(Priority.LOW),
                ParcelUtils.getParcel(Priority.LOW)
        );
    }

    @Test
    void selectParcelHighPriority() {
        when(parcelRepository.findParcelByPriority(Priority.HIGH)).thenReturn(parcelHighPriorityList);
        assertEquals(Priority.HIGH, parcelSelectable.selectParcelHighPriority().get().getPriority());
    }

    @Test
    void selectParcelMediumPriority() {
        when(parcelRepository.findParcelByPriority(Priority.MEDIUM)).thenReturn(parcelMediumPriorityList);
        assertEquals(Priority.MEDIUM, parcelSelectable.selectParcelMediumPriority().get().getPriority());
    }

    @Test
    void selectParcelLowPriority() {
        when(parcelRepository.findParcelByPriority(Priority.LOW)).thenReturn(parcelLowPriorityList);
        assertEquals(Priority.LOW, parcelSelectable.selectParcelLowPriority().get().getPriority());
    }
}