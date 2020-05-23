package com.courier.controller;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.enums.UserType;
import com.courier.security.CourierUserDetailsService;
import com.courier.service.CourierUserService;
import com.courier.utils.UserUtils;
import com.courier.validators.CourierUserValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("courier-user")
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CourierUserController.class)
public class CourierUserControllerTest {

    public static final String BASE_PATH = "/api/v1/";

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private CourierUserValidator courierUserValidator;

    @MockBean
    private CourierUserService courierUserService;

    @MockBean
    private CourierUserDetailsService courierUserDetailsService;

    @DisplayName("Create courier user")
    @Test
    public void shouldCreateCourierUser() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);

        mockMvc.perform(post(BASE_PATH + "courier-users")
        .accept(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(new ObjectMapper().writeValueAsBytes(courierUserRequestDto)))
                .andDo(print())
        .andExpect(status().isCreated());
    }

    @DisplayName("Create courier user via XML")
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

    @DisplayName("Attempt to create a courier user with an invalid password")
    @TestFactory
    public Stream<DynamicTest> shouldNotCreateUserWithInvalidPassword() throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);

        Stream<String> courierUserRequestDtoStream = Stream.of("moref", "  ", "", null);

        return courierUserRequestDtoStream.map(
                password ->
                    DynamicTest.dynamicTest("Invalid password of " + password, () -> {
                        courierUserRequestDto.setPassword(password);

                        mockMvc.perform(post(BASE_PATH + "courier-users")
                                .accept(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(new ObjectMapper().writeValueAsBytes(courierUserRequestDto)))
                                .andDo(print())
                                .andExpect(status().isBadRequest());
                    })
                );
    }

    @DisplayName("Attempt to create a user with a blank email")
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

    @DisplayName("Attempt to create a user with an invalid email")
    @ParameterizedTest
    @ValueSource(strings = {"email.google.com", "@google.com", "email"} )
    public void shouldNotCreateUserWithInvalidEmail(String email) throws Exception {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        courierUserRequestDto.setEmail(email);

        mockMvc.perform(post(BASE_PATH + "courier-users")
                .accept(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(new ObjectMapper().writeValueAsBytes(courierUserRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Attempt to create a user with an invalid type")
    @Test
    public void shouldNotCreateUserWithInvalidType() throws Exception {
        final String entity = "{\n" +
                "    \"email\": \"jerry@courier.com\",\n" +
                "    \"password\": \"password\",\n" +
                "    \"types\": [\"DR\"]\n" +
                "}";

        mockMvc.perform(post(BASE_PATH + "courier-users")
                .accept(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(entity))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Attempt to create a user with an invalid type with spaces")
    @Test
    public void shouldNotCreateUserWithInvalidTypeWithSpaces() throws Exception {
        final String entity = "{\n" +
                "    \"email\": \"jerry@courier.com\",\n" +
                "    \"password\": \"password\",\n" +
                "    \"types\": [\"DRIVER \"]\n" +
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