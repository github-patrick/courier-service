package com.courier.repository;

import com.courier.domain.CourierUser;
import com.courier.domain.dtos.CourierUserResponseDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourierUserRepository extends CrudRepository<CourierUser,Long> {

    Optional<CourierUser> findByEmail(String email);
}
