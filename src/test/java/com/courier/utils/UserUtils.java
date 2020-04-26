package com.courier.utils;

import com.courier.domain.CourierUser;
import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.enums.UserType;

public class UserUtils {

    public static CourierUserRequestDto getUser(UserType userType) {
        return CourierUserRequestDto.builder().email("random@courier.com")
                .password("password").userType(userType).build();
    }

    public static CourierUserResponseDto getUserResponseDto(UserType userType) {
        return CourierUserResponseDto.builder().id(1l).email("random@courier.com")
                .password("password").userType(userType).build();
    }

    public static CourierUserResponseDto getUserCustomerResponseDto() {
        return CourierUserResponseDto.builder().id(1l).email("random@courier.com")
                .password("password").userType(UserType.CUSTOMER).build();
    }

    public static CourierUserResponseDto getUserDriverResponseDto() {
        return CourierUserResponseDto.builder().id(1l).email("random@courier.com")
                .password("password").userType(UserType.DRIVER).build();
    }

}
