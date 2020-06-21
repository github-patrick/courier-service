package com.courier.service;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.exception.CannotRegisterUserException;
import com.courier.exception.CourierUserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface CourierUserService {

    CourierUserResponseDto addCourierUser(CourierUserRequestDto courierUserRequestDto) throws CannotRegisterUserException;

    List<CourierUserResponseDto> getAllCourierUsers();

    CourierUserResponseDto getCourierUserByEmail(String email) throws CourierUserNotFoundException;

    CourierUserResponseDto getCourierUserById(Long id) throws CourierUserNotFoundException;

}
