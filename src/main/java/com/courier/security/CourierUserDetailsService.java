package com.courier.security;

import com.courier.domain.CourierUser;
import com.courier.repository.CourierUserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.UnknownServiceException;

@Service
public class CourierUserDetailsService implements UserDetailsService {

    @Autowired
    private CourierUserRepository courierUserRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        CourierUser courierUser = courierUserRepository.findByEmail(email)
                .orElseThrow(UnknownServiceException::new);

        return User.builder().username(courierUser.getEmail())
                .password(courierUser.getPassword())
                .roles(courierUser.getUserType().toString()).build();
    }
}
