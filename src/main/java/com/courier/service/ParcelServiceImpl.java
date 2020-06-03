package com.courier.service;

import com.courier.domain.Parcel;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.dtos.ParcelRequestDto;
import com.courier.domain.dtos.ParcelResponseDto;
import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import com.courier.exception.ParcelNotFoundException;
import com.courier.repository.ParcelRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public ParcelResponseDto getParcel(String id) throws ParcelNotFoundException {

        log.info("Attempting to get parcel with id {}", id);

        Optional<Parcel> parcel = parcelRepository.findById(id);
        if (!parcel.isPresent()) {
            throw new ParcelNotFoundException("Parcel of id " + id + " does not exist");
        }
        return modelMapper.map(parcel.get(), ParcelResponseDto.class);
    }

    @Override
    public List<ParcelResponseDto> getAllParcels(ParcelStatus parcelStatus, Priority priority) {
        List<ParcelResponseDto> parcelResponseDtoList = new ArrayList<>();

        if (parcelStatus != null && priority != null) {
            parcelRepository.findParcelByStatusAndPriority(parcelStatus, priority).forEach(parcel ->
                    parcelResponseDtoList.add(modelMapper.map(parcel,ParcelResponseDto.class)));
            return parcelResponseDtoList; }
        if (parcelStatus == null && priority != null) {
            parcelRepository.findParcelByPriority(priority).forEach(parcel ->
                    parcelResponseDtoList.add(modelMapper.map(parcel,ParcelResponseDto.class)));
            return parcelResponseDtoList; }
        if (parcelStatus != null && priority == null) {
            parcelRepository.findParcelByStatus(parcelStatus).forEach(parcel ->
                    parcelResponseDtoList.add(modelMapper.map(parcel,ParcelResponseDto.class)));
            return parcelResponseDtoList; }

        parcelRepository.findAll().forEach(parcel ->
                parcelResponseDtoList.add(modelMapper.map(parcel,ParcelResponseDto.class)));
        return parcelResponseDtoList;
    }

    @Override
    public void updateParcel(Map<String,String> httpRequestBody, String id) throws ParcelNotFoundException {
        ParcelResponseDto parcelResponseDto = getParcel(id);
        String status = httpRequestBody.get("status");
        if (!StringUtils.isEmpty(status)) {
            parcelResponseDto.setStatus(ParcelStatus.valueOf(status));}
        Parcel parcelMapped = parcelRepository.save(modelMapper.map(parcelResponseDto, Parcel.class));
        parcelRepository.save(parcelMapped);
    }


    private boolean checkIfParcelExists(String id) {
        return parcelRepository.existsById(id);
    }
}
