package com.courier.service;

import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.dtos.ParcelRequestDto;
import com.courier.domain.dtos.ParcelResponseDto;
import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import com.courier.exception.ParcelNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ParcelService {

    ParcelResponseDto addParcel(ParcelRequestDto parcelRequestDto, CustomerResponseDto customerResponseDto);

    ParcelResponseDto getParcel(String id) throws ParcelNotFoundException;

    List<ParcelResponseDto> getAllParcels(ParcelStatus parcelStatus, Priority priority);

    void updateParcel(Map<String,String> requestBody, String id) throws ParcelNotFoundException;

}
