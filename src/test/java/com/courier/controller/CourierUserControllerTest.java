package com.courier.controller;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.enums.UserType;
import com.courier.security.CourierUserDetailsService;
import com.courier.service.CourierUserService;
import com.courier.utils.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = CourierUserController.class)
public class CourierUserControllerTest {

    public static final String BASE_PATH = "/api/v1/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierUserService courierUserService;

    @MockBean
    private CourierUserDetailsService courierUserDetailsService;

    @Test
    public void shouldCreateCourierUser() throws Exception {

        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);

        mockMvc.perform(post(BASE_PATH + "courier-users")
        .accept(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(new ObjectMapper().writeValueAsBytes(courierUserRequestDto)))
        .andExpect(status().isCreated());
    }

    @Test
    public void shouldCreateUserViaXml() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);


        when(courierUserService.addCourierUser(any(CourierUserRequestDto.class))).thenReturn(UserUtils.getUserCustomerResponseDto());

        mockMvc.perform(post(BASE_PATH + "courier-users")
                .accept(MediaType.APPLICATION_XML_VALUE)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(new XmlMapper().writeValueAsBytes(courierUserRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(xpath("/CourierUserResponseDto/email/text()").exists());
    }

    @Test
    public void shouldNotCreateUserWithInvalidPassword() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        courierUserRequestDto.setPassword("bass");

        mockMvc.perform(post(BASE_PATH + "courier-users")
                .accept(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(new ObjectMapper().writeValueAsBytes(courierUserRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateUserWithBlankEmail() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        courierUserRequestDto.setEmail("");

        mockMvc.perform(post(BASE_PATH + "courier-users")
                .accept(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(new ObjectMapper().writeValueAsBytes(courierUserRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateUserWithInvalidEmail() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        courierUserRequestDto.setEmail("email.google.com");

        mockMvc.perform(post(BASE_PATH + "courier-users")
                .accept(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(new ObjectMapper().writeValueAsBytes(courierUserRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateUserWithInvalidType() throws Exception {
        final String entity = "{\n" +
                "    \"email\": \"jerry@courier.com\",\n" +
                "    \"password\": \"password\",\n" +
                "    \"types\": \"[\"CUSTOMER\"]\"\n" +
                "}";

        mockMvc.perform(post(BASE_PATH + "courier-users")
                .accept(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(entity))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}