package com.courier.controller;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerRequestDto;
import com.courier.domain.enums.UserType;
import com.courier.security.CourierUserDetailsService;
import com.courier.service.CourierUserService;
import com.courier.service.CustomerService;
import com.courier.utils.CustomerUtils;
import com.courier.utils.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
@WithMockUser(roles = "CUSTOMER")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourierUserService courierUserService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CourierUserDetailsService courierUserDetailsService;

    @Test
    public void createCustomerProfile() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        customerRequestDto.setEmail(courierUserRequestDto.getEmail());

        mockMvc.perform(post("/api/v1/customers")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
        .content(objectMapper.writeValueAsBytes(customerRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createCustomerProfileViaXml() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        customerRequestDto.setEmail(courierUserRequestDto.getEmail());

        mockMvc.perform(post("/api/v1/customers")
                .accept(MediaType.APPLICATION_XML_VALUE)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .characterEncoding("UTF-8")
                .content(new XmlMapper().writeValueAsBytes(customerRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithAnonymousUser
    public void createCustomerProfileUnauthenticated() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        customerRequestDto.setEmail(courierUserRequestDto.getEmail());

        mockMvc.perform(post("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(customerRequestDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createCustomerProvideUnauthorised() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        customerRequestDto.setEmail(courierUserRequestDto.getEmail());

        mockMvc.perform(post("/api/v1/customers").with(user("hello").password("password").roles("FAKE_ROLE"))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(customerRequestDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void createAttemptCustomerWithInvalidFullName() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        customerRequestDto.setEmail(courierUserRequestDto.getEmail());
        customerRequestDto.setFullName("");

        mockMvc.perform(post("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(customerRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createAttemptCustomerWithInvalidEmail() throws Exception {
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        customerRequestDto.setEmail("malformed.email.com");

        mockMvc.perform(post("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(customerRequestDto)))
                .andExpect(status().isBadRequest());
    }



}