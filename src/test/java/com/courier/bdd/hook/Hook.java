package com.courier.bdd.hook;

import com.courier.CourierServiceApplication;
import com.courier.config.AppConfig;
import com.courier.repository.CourierUserRepository;
import io.cucumber.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CourierServiceApplication.class, AppConfig.class})
@Slf4j
public class Hook {

    @Autowired
    private CourierUserRepository courierUserRepository;

    @Before
    public void setUp() {
        courierUserRepository.deleteAll();
    }
}
