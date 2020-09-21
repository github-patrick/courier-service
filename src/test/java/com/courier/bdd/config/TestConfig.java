package com.courier.bdd.config;

import com.courier.bdd.Context;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public Context context() {
        return new Context();
    }
}
