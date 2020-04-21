package com.courier.service;

import com.courier.domain.CourierUser;
import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.repository.CourierUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourierUserServiceImpl implements CourierUserService {

    private ModelMapper modelMapper;
    private CourierUserRepository courierUserRepository;

    @Autowired
    public CourierUserServiceImpl(ModelMapper modelMapper, CourierUserRepository courierUserRepository) {
        this.modelMapper = modelMapper;
        this.courierUserRepository = courierUserRepository;
    }

    @Override
    public CourierUserResponseDto addCourierUser(CourierUserRequestDto courierUserRequestDto) {
        CourierUser courierUserSaved = courierUserRepository.save(modelMapper
                .map(courierUserRequestDto, CourierUser.class));
        return modelMapper.map(courierUserSaved, CourierUserResponseDto.class);
    }

    @Override
    public List<CourierUserResponseDto> getAllCourierUsers() {
        List<CourierUserResponseDto> courierUserResponseDtoList =new ArrayList();
        courierUserRepository.findAll().forEach(courierUser ->
                courierUserResponseDtoList.add(modelMapper.map(courierUser,CourierUserResponseDto.class))
        );
        return courierUserResponseDtoList;
    }
}
