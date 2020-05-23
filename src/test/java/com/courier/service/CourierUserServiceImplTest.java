package com.courier.service;

import com.courier.domain.CourierUser;
import com.courier.domain.Customer;
import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.enums.UserType;
import com.courier.exception.CannotRegisterUserException;
import com.courier.exception.CourierUserNotFoundException;
import com.courier.repository.CourierUserRepository;
import com.courier.utils.UserUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("courier-user")
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
    public void shouldCreateUser() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        given(courierUserRepository.save(ArgumentMatchers.any(CourierUser.class))).willReturn(new CourierUser());
        assertNotNull(courierUserService.addCourierUser(courierUserRequestDto));
    }

    @Test
    public void shouldNotCreateDuplicateUsers() {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        when(courierUserRepository.findByEmail(courierUserRequestDto.getEmail())).thenReturn(Optional.of(new CourierUser()));
        assertThrows(CannotRegisterUserException.class, ()->courierUserService.addCourierUser(courierUserRequestDto));
    }


    @Test
    public void getAllUsers() {
        given(courierUserRepository.findAll()).willReturn(Arrays.asList(new CourierUser()));
        List<CourierUserResponseDto> courierUsers = courierUserService.getAllCourierUsers();
        assertThat(courierUsers.size(), equalTo(1));
    }

    @Test
    public void getUserByEmail() throws CourierUserNotFoundException {
        final String email = "developer@courier.com";

        CourierUser courierUser = CourierUser.superBuilder().id(34L).userType(Arrays.asList(UserType.CUSTOMER)).password("password")
                .createdAt(new Date()).modifiedAt(new Date()).email(email).build();

        given(courierUserRepository.findByEmail(email)).willReturn(Optional.of(courierUser));
        CourierUserResponseDto courierUserResponseDto = courierUserService.getCourierUserByEmail(email);
        assertThat(courierUserResponseDto.getEmail(), equalTo(email));
    }

}