package com.courier.service;

import com.courier.domain.CourierUser;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.repository.CourierUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CourierUserServiceImplTest {

    @Mock
    private CourierUserRepository courierUserRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private CourierUserServiceImpl courierUserService= new CourierUserServiceImpl(modelMapper, courierUserRepository);

    @Test
    public void getAllUsers() {
        given(courierUserRepository.findAll()).willReturn(Arrays.asList(new CourierUser()));
        List<CourierUserResponseDto> courierUsers = courierUserService.getAllCourierUsers();
        assertThat(courierUsers.size(), equalTo(1));
    }

}