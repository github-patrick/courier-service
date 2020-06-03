package com.courier.service;

import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import com.courier.repository.ParcelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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