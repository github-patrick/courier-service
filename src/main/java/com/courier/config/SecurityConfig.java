package com.courier.config;

import com.courier.security.CourierUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CourierUserDetailsService courierUserDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.

                 authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()

                .antMatchers("/api/v1/courier-users").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/v1/healthcheck").permitAll()
                .antMatchers("/api/v1/courier-users/**")
                .hasAnyRole("CUSTOMER","ADMIN","DRIVER")
                .antMatchers("/api/v1/customers")
                    .hasAnyRole("CUSTOMER","ADMIN")
                .antMatchers("/api/v1/drivers")
                .hasAnyRole("DRIVER","ADMIN")
                .antMatchers("/api/v1/customers/**")
                    .hasAnyRole("CUSTOMER")
                .antMatchers("/api/v1/parcels/**")
                    .hasAnyRole("CUSTOMER","ADMIN")
                .antMatchers("/api/v1/**")
                .authenticated().and()
                .httpBasic().
                and().formLogin();


        http.csrf().disable();
        http.headers().frameOptions().disable();
    }


    @Autowired
    public void globalConfigure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(courierUserDetailsService);
    }
}
