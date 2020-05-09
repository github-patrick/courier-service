package com.courier.security;

import com.courier.domain.CourierUser;
import com.courier.domain.enums.UserType;
import com.courier.repository.CourierUserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CourierUserDetailsService implements UserDetailsService {

    @Autowired
    private CourierUserRepository courierUserRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        CourierUser courierUser = courierUserRepository.findByEmail(email)
                .orElseThrow(UnknownServiceException::new);

        int sizeOfArray = courierUser.getTypes().size();

        String[] types = new String[sizeOfArray];
        courierUser.getTypes().forEach(type -> {
            for(int i=0;i<sizeOfArray;i++) {
                types[i] = type.toString();
            }
        });
        return User.builder().username(courierUser.getEmail())
                .password(courierUser.getPassword())
                .roles(types).build();

    }
}
