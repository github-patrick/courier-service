package com.courier.service;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface CourierUserService {

    CourierUserResponseDto addCourierUser(CourierUserRequestDto courierUserRequestDto);

    List<CourierUserResponseDto> getAllCourierUsers();

    CourierUserResponseDto getCourierUserByEmail(String email);

}
