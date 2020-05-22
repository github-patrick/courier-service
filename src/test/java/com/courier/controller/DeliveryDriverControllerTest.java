package com.courier.controller;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.DeliveryDriverRequestDto;
import com.courier.domain.dtos.DeliveryDriverResponseDto;
import com.courier.domain.dtos.DeliveryDriverStatusDto;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.exception.CannotCreateDriverProfileException;
import com.courier.exception.CourierUserNotFoundException;
import com.courier.exception.DeliveryDriverNotFoundException;
import com.courier.security.CourierUserDetailsService;
import com.courier.service.CourierUserService;
import com.courier.service.DeliveryDriverService;
import com.courier.utils.DeliveryDriverUtils;
import com.courier.utils.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WithMockUser(roles = "DRIVER", username = "user@courier.com")
@WebMvcTest(controllers = DeliveryDriverController.class)
public class
DeliveryDriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourierUserService courierUserService;

    @MockBean
    private DeliveryDriverService deliveryDriverService;

    @MockBean
    private CourierUserDetailsService courierUserDetailsService;

    @Test
    public void createDeliveryDriverProfile() throws Exception {
        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto();
        CourierUserResponseDto userDriverResponseDto = UserUtils.getUserDriverResponseDto();

        when(courierUserService.getCourierUserByEmail(deliveryDriverRequestDto.getEmail())).thenReturn(userDriverResponseDto);

        when(deliveryDriverService.addDeliveryDriver(any(DeliveryDriverRequestDto.class), any(CourierUserResponseDto.class)))
                .thenReturn(DeliveryDriverUtils.getDeliveryDriverResponseDto(userDriverResponseDto));

        mockMvc.perform(post("/api/v1/drivers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(deliveryDriverRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deliveryDriverStatus", is("UNAVAILABLE")));
    }

    @Test
    public void createDeliveryDriverProfileViaXml() throws Exception {
        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto();
        CourierUserResponseDto userDriverResponseDto = UserUtils.getUserDriverResponseDto();

        when(courierUserService.getCourierUserByEmail(deliveryDriverRequestDto.getEmail())).thenReturn(userDriverResponseDto);

        when(deliveryDriverService.addDeliveryDriver(any(DeliveryDriverRequestDto.class), any(CourierUserResponseDto.class)))
                .thenReturn(DeliveryDriverUtils.getDeliveryDriverResponseDto(userDriverResponseDto));

        mockMvc.perform(post("/api/v1/drivers")
                .accept(MediaType.APPLICATION_XML_VALUE)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .characterEncoding("UTF-8")
                .content(new XmlMapper().writeValueAsBytes(deliveryDriverRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(xpath("/DeliveryDriverResponseDto/deliveryDriverStatus/text()").exists());
    }

    @Test
    public void createDriverProfileWithCourierUserNotFoundException() throws Exception {
        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto();

        when(courierUserService.getCourierUserByEmail(deliveryDriverRequestDto.getEmail())).thenThrow(CourierUserNotFoundException.class);

        mockMvc.perform(post("/api/v1/drivers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(deliveryDriverRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("Bad Request")));
    }

    @Test
    public void createDriverProfileWithCannotCreateDriverProfileException() throws Exception {
        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto();
        CourierUserResponseDto userDriverResponseDto = UserUtils.getUserDriverResponseDto();

        when(courierUserService.getCourierUserByEmail(deliveryDriverRequestDto.getEmail())).thenReturn(userDriverResponseDto);

        when(deliveryDriverService.addDeliveryDriver(any(DeliveryDriverRequestDto.class), any(CourierUserResponseDto.class)))
                .thenThrow(new CannotCreateDriverProfileException("User is not a driver. Register as a driver and retry."));

        mockMvc.perform(post("/api/v1/drivers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(deliveryDriverRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("Bad Request")));
    }


    @ParameterizedTest
    @ValueSource(strings = {"", " ",})
    public void createDeliveryDriverProfileInvalidFullName(String fullName) throws Exception {
        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto();
        CourierUserResponseDto userDriverResponseDto = UserUtils.getUserDriverResponseDto();

        deliveryDriverRequestDto.setFullName(fullName);

        when(courierUserService.getCourierUserByEmail(deliveryDriverRequestDto.getEmail())).thenReturn(userDriverResponseDto);

        when(deliveryDriverService.addDeliveryDriver(any(DeliveryDriverRequestDto.class), any(CourierUserResponseDto.class)))
                .thenReturn(DeliveryDriverUtils.getDeliveryDriverResponseDto(userDriverResponseDto));

        mockMvc.perform(post("/api/v1/drivers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(deliveryDriverRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("Bad Request")));
    }

    @Test
    @WithAnonymousUser
    public void createDeliveryDriverProfileUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/drivers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(new DeliveryDriverRequestDto())))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void createDeliveryDriverProfileForbidden() throws Exception {

        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto();

        mockMvc.perform(post("/api/v1/drivers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(deliveryDriverRequestDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void createDeliveryDriverProfileInvalidEmail() throws Exception {

        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto();
        deliveryDriverRequestDto.setEmail("invalid-email@");

        mockMvc.perform(post("/api/v1/drivers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(deliveryDriverRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("Bad Request")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void listAllDeliveryDrivers() throws Exception {

        when(deliveryDriverService.getAllDeliveryDrivers())
                .thenReturn(Arrays.asList(DeliveryDriverUtils.getDeliveryDriverResponseDto(UserUtils.getUserDriverResponseDto()),
                        DeliveryDriverUtils.getDeliveryDriverResponseDto(UserUtils.getUserDriverResponseDto())));
        deliveryDriverService.getAllDeliveryDrivers();

        mockMvc.perform(get("/api/v1/drivers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void listAllDeliveryDriversInvalidRole() throws Exception {

        when(deliveryDriverService.getAllDeliveryDrivers())
                .thenReturn(Arrays.asList(DeliveryDriverUtils.getDeliveryDriverResponseDto(UserUtils.getUserDriverResponseDto()),
                        DeliveryDriverUtils.getDeliveryDriverResponseDto(UserUtils.getUserDriverResponseDto())));
        deliveryDriverService.getAllDeliveryDrivers();

        mockMvc.perform(get("/api/v1/drivers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void listAllDeliveryDriversFilteredByStatus() throws Exception {
        when(deliveryDriverService.getAllDeliveryDrivers(DeliveryDriverStatus.UNAVAILABLE))
                .thenReturn(Arrays.asList(DeliveryDriverUtils.getDeliveryDriverResponseDto(UserUtils.getUserDriverResponseDto()),
                        DeliveryDriverUtils.getDeliveryDriverResponseDto(UserUtils.getUserDriverResponseDto())));
        deliveryDriverService.getAllDeliveryDrivers();

        mockMvc.perform(get("/api/v1/drivers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("status", DeliveryDriverStatus.UNAVAILABLE.toString())
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "INVALID_ENUM"})
    @WithMockUser(roles = "ADMIN")
    public void listAllDeliveryDriversFilteredByInvalidStatus(String enumType) throws Exception {
        when(deliveryDriverService.getAllDeliveryDrivers(any(DeliveryDriverStatus.class)))
                .thenReturn(Arrays.asList(DeliveryDriverUtils.getDeliveryDriverResponseDto(UserUtils.getUserDriverResponseDto()),
                        DeliveryDriverUtils.getDeliveryDriverResponseDto(UserUtils.getUserDriverResponseDto())));

        mockMvc.perform(get("/api/v1/drivers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("status", enumType)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));
    }

    @Test
    public void getDriverById() throws Exception {
        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserDriverResponseDto();
        when(deliveryDriverService.getDeliveryDriver(1L))
                .thenReturn(DeliveryDriverUtils.getDeliveryDriverResponseDto(courierUserResponseDto));

        mockMvc.perform(get("/api/v1/drivers/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryDriverStatus", is(DeliveryDriverStatus.UNAVAILABLE.toString())));
    }

    @Test
    public void getDriverByIdViaXml() throws Exception {
        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserDriverResponseDto();
        when(deliveryDriverService.getDeliveryDriver(1L))
                .thenReturn(DeliveryDriverUtils.getDeliveryDriverResponseDto(courierUserResponseDto));

        mockMvc.perform(get("/api/v1/drivers/{id}", 1L)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("/DeliveryDriverResponseDto/deliveryDriverStatus/text()").exists());
    }

    @Test
    public void getDriverDoesNotExist() throws Exception {

        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserDriverResponseDto();
        when(deliveryDriverService.getDeliveryDriver(134L))
                .thenThrow(DeliveryDriverNotFoundException.class);

        mockMvc.perform(get("/api/v1/drivers/{id}", 134L)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.getReasonPhrase())));
    }

    @Test
    public void updateDriverStatus() throws Exception {

        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserDriverResponseDto();
        courierUserResponseDto.setEmail("user@courier.com");
        DeliveryDriverResponseDto deliveryDriverResponseDto = DeliveryDriverUtils.getDeliveryDriverResponseDto(courierUserResponseDto);
        DeliveryDriverStatusDto deliveryDriverStatus =
                DeliveryDriverStatusDto.builder().deliveryDriverStatus(DeliveryDriverStatus.AVAILABLE).build();


        when(deliveryDriverService.getDeliveryDriver(1L)).thenReturn(deliveryDriverResponseDto);


        mockMvc.perform(patch("/api/v1/drivers/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(deliveryDriverStatus)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateWithInvalidDriverStatus() throws Exception {

        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserDriverResponseDto();
        courierUserResponseDto.setEmail("user@courier.com");
        DeliveryDriverResponseDto deliveryDriverResponseDto = DeliveryDriverUtils.getDeliveryDriverResponseDto(courierUserResponseDto);
        DeliveryDriverStatusDto deliveryDriverStatus =
                DeliveryDriverStatusDto.builder().deliveryDriverStatus(DeliveryDriverStatus.AVAILABLE).build();


        when(deliveryDriverService.getDeliveryDriver(1L)).thenReturn(deliveryDriverResponseDto);


        mockMvc.perform(patch("/api/v1/drivers/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content("{\"deliveryDriverStatus\":\"AVAILABLE_ERROR\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void driverCanOnlyUpdateTheirStatus() throws Exception {

        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserDriverResponseDto();
        DeliveryDriverResponseDto deliveryDriverResponseDto = DeliveryDriverUtils.getDeliveryDriverResponseDto(courierUserResponseDto);
        DeliveryDriverStatusDto deliveryDriverStatus =
                DeliveryDriverStatusDto.builder().deliveryDriverStatus(DeliveryDriverStatus.AVAILABLE).build();

        when(deliveryDriverService.getDeliveryDriver(1L)).thenReturn(deliveryDriverResponseDto);

        mockMvc.perform(patch("/api/v1/drivers/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(deliveryDriverStatus)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
