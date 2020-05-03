package com.courier.service;

import com.courier.domain.CourierUser;
import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.exception.CourierUserNotFoundException;
import com.courier.repository.CourierUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CourierUserServiceImpl implements CourierUserService {

    private ModelMapper modelMapper;
    private CourierUserRepository courierUserRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public CourierUserServiceImpl(ModelMapper modelMapper, CourierUserRepository courierUserRepository,
                                  BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.modelMapper = modelMapper;
        this.courierUserRepository = courierUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public CourierUserResponseDto addCourierUser(CourierUserRequestDto courierUserRequestDto) {

        log.info("Creating courier user");

        courierUserRequestDto.setPassword(bCryptPasswordEncoder.encode(courierUserRequestDto.getPassword()));
        CourierUser courierUserSaved = courierUserRepository.save(modelMapper
                .map(courierUserRequestDto, CourierUser.class));

        log.info("Courier user persisted to the database");
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

    @Override
    public CourierUserResponseDto getCourierUserByEmail(String email) throws CourierUserNotFoundException {
        Optional<CourierUser> courierUser = courierUserRepository.findByEmail(email);
        if (!courierUser.isPresent()) {
            log.info("Unable to retrieve " + email);
            throw new CourierUserNotFoundException("Courier user not found for " + email);
        }
        return modelMapper.map(courierUser.get(), CourierUserResponseDto.class);
    }
}
