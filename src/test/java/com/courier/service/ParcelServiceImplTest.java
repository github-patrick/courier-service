package com.courier.service;

import com.courier.domain.Parcel;
import com.courier.domain.dtos.ParcelResponseDto;
import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import com.courier.exception.ParcelNotFoundException;
import com.courier.repository.ParcelRepository;
import com.courier.utils.CustomerUtils;
import com.courier.utils.ParcelUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParcelServiceImplTest {

    @Mock
    private ParcelRepository parcelRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private ParcelService parcelServiceImpl = new ParcelServiceImpl(parcelRepository,modelMapper);

    @Test
    void getAllParcels() {
        parcelServiceImpl.getAllParcels(ParcelStatus.NOT_DISPATCHED, Priority.LOW);
        verify(parcelRepository, times(1))
                .findParcelByStatusAndPriority(any(ParcelStatus.class),any(Priority.class));
        verify(parcelRepository, times(0))
                .findParcelByStatus(any(ParcelStatus.class));
        verify(parcelRepository, times(0))
                .findParcelByPriority(any(Priority.class));
    }
}