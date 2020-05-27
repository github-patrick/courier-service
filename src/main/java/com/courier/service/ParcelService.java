package com.courier.service;

import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.dtos.ParcelRequestDto;
import com.courier.domain.dtos.ParcelResponseDto;

public interface ParcelService {

    ParcelResponseDto addParcel(ParcelRequestDto parcelRequestDto, CustomerResponseDto customerResponseDto);
}
