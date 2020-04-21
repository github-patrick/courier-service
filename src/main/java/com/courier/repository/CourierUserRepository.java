package com.courier.repository;

import com.courier.domain.CourierUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierUserRepository extends CrudRepository<CourierUser,Long> {

}
