package com.courier.service;

import com.courier.domain.Parcel;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.dtos.ParcelRequestDto;
import com.courier.domain.dtos.ParcelResponseDto;
import com.courier.domain.enums.Priority;
import com.courier.repository.ParcelRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ParcelServiceImpl implements ParcelService {

    private ParcelRepository parcelRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ParcelServiceImpl(ParcelRepository parcelRepository, ModelMapper modelMapper) {
        this.parcelRepository = parcelRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ParcelResponseDto addParcel(ParcelRequestDto parcelRequestDto, CustomerResponseDto customerResponseDto) {
        parcelRequestDto.setSender(customerResponseDto);
        if (parcelRequestDto.getPriority() == null) {
            parcelRequestDto.setPriority(Priority.MEDIUM);
        }
        log.info("Attempting to create a parcel for delivery");
        Parcel parcelSaved = parcelRepository.save(modelMapper.map(parcelRequestDto, Parcel.class));
        log.info("Parcel of id: {}", parcelSaved.getId());
        return modelMapper.map(parcelSaved,ParcelResponseDto.class);
    }
}
