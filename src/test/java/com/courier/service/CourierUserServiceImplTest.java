package com.courier.service;

import com.courier.domain.CourierUser;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.enums.UserType;
import com.courier.exception.CourierUserNotFoundException;
import com.courier.repository.CourierUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CourierUserServiceImplTest {

    @Mock
    private CourierUserRepository courierUserRepository;

    @Spy
    private ModelMapper modelMapper;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private CourierUserServiceImpl courierUserService= new CourierUserServiceImpl(modelMapper, courierUserRepository, bCryptPasswordEncoder);

    @Test
    public void getAllUsers() {
        given(courierUserRepository.findAll()).willReturn(Arrays.asList(new CourierUser()));
        List<CourierUserResponseDto> courierUsers = courierUserService.getAllCourierUsers();
        assertThat(courierUsers.size(), equalTo(1));
    }

    @Test
    public void getUserByEmail() throws CourierUserNotFoundException {
        final String email = "developer@courier.com";

        CourierUser courierUser = CourierUser.superBuilder().id(34L).userType(UserType.CUSTOMER).password("password")
                .createdAt(new Date()).modifiedAt(new Date()).email(email).build();

        given(courierUserRepository.findByEmail(email)).willReturn(Optional.of(courierUser));
        CourierUserResponseDto courierUserResponseDto = courierUserService.getCourierUserByEmail(email);
        assertThat(courierUserResponseDto.getEmail(), equalTo(email));
    }

}