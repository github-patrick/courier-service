package com.courier.controller;

import com.courier.domain.Parcel;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.dtos.ParcelRequestDto;
import com.courier.domain.dtos.ParcelResponseDto;
import com.courier.domain.enums.ParcelStatus;
import com.courier.exception.CustomerNotFoundException;
import com.courier.exception.ParcelNotFoundException;
import com.courier.security.CourierUserDetailsService;
import com.courier.service.CustomerService;
import com.courier.service.ParcelService;
import com.courier.utils.CustomerUtils;
import com.courier.utils.ParcelUtils;
import com.courier.utils.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@Tag("parcel")
@WebMvcTest(controllers = ParcelController.class)
@ExtendWith(SpringExtension.class)
@WithMockUser(roles = "CUSTOMER", username = ParcelControllerTest.EMAIL)
public class ParcelControllerTest {

    public static final String EMAIL = "user@courier.com";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourierUserDetailsService courierUserDetailsService;

    @MockBean
    private ParcelService parcelService;

    @MockBean
    private CustomerService customerService;

    private XmlMapper xmlMapper = new XmlMapper();

    @DisplayName("create parcel to send to a destination")
    @Test
    public void createParcelToSend() throws Exception {

        ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto();
        CustomerResponseDto customerResponseDto = CustomerUtils.getCustomerResponseDto(UserUtils.getUserCustomerResponseDto());
        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserCustomerResponseDto();
        courierUserResponseDto.setEmail("user@courier.com");
        customerResponseDto.setCourierUser(courierUserResponseDto);

        when(customerService.getCustomer(1L)).thenReturn(customerResponseDto);
        when(parcelService.addParcel(any(ParcelRequestDto.class),any(CustomerResponseDto.class)))
                .thenReturn(ParcelUtils.getParcelResponseDto(customerResponseDto));

        mockMvc.perform(post("/api/v1/customers/{id}/parcels", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(parcelRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.status").exists());
    }

    @DisplayName("create parcel to send to a destination via xml")
    @Test
    public void createParcelToSendViaXml() throws Exception {

        ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto();
        CustomerResponseDto customerResponseDto = CustomerUtils.getCustomerResponseDto(UserUtils.getUserCustomerResponseDto());
        CourierUserResponseDto courierUserResponseDto = UserUtils.getUserCustomerResponseDto();
        courierUserResponseDto.setEmail("user@courier.com");
        customerResponseDto.setCourierUser(courierUserResponseDto);

        when(customerService.getCustomer(1L)).thenReturn(customerResponseDto);
        when(parcelService.addParcel(any(ParcelRequestDto.class),any(CustomerResponseDto.class)))
                .thenReturn(ParcelUtils.getParcelResponseDto(customerResponseDto));

        mockMvc.perform(post("/api/v1/customers/{id}/parcels", 1L)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .characterEncoding("UTF-8")
                .content(xmlMapper.writeValueAsBytes(parcelRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(xpath("/ParcelResponseDto/id").exists())
                .andExpect(xpath("/ParcelResponseDto/priority").exists())
                .andExpect(xpath("/ParcelResponseDto/status").string("NOT_DISPATCHED"));
    }

    @DisplayName("Add parcel error cases")
    @Nested
    @WithMockUser(roles = "CUSTOMER")
    class AddParcelErrorCases {

        @DisplayName("Should not create invalid parcel for delivery where the destination is not set")
        @Test
        public void attemptToCreateInvalidParcel() throws Exception {

            ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto();
            parcelRequestDto.setDestination(null);

            mockMvc.perform(post("/api/v1/customers/{id}/parcels", 1L)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsBytes(parcelRequestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("Should not create invalid parcel for delivery where the destination is blank")
        @Test
        public void attemptToCreateInvalidParcelBlankDestination() throws Exception {

            ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto();
            parcelRequestDto.setDestination("");

            mockMvc.perform(post("/api/v1/customers/{id}/parcels", 1L)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsBytes(parcelRequestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("Should not create invalid parcel for delivery where the origin is not set")
        @Test
        public void attemptToCreateInvalidParcelOriginNotSet() throws Exception {

            ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto();
            parcelRequestDto.setOrigin(null);

            mockMvc.perform(post("/api/v1/customers/{id}/parcels", 1L)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsBytes(parcelRequestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("Should not create invalid parcel for delivery where the origin is blank")
        @Test
        public void attemptToCreateInvalidParcelBlankOriginDestination() throws Exception {

            ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto();
            parcelRequestDto.setOrigin("");

            mockMvc.perform(post("/api/v1/customers/{id}/parcels", 1L)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsBytes(parcelRequestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("Customer does not exist to send a parcel")
        @Test
        public void customerDoesNotExistToAddParcel() throws Exception {

            ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto();
            when(customerService.getCustomer(123L)).thenThrow(new CustomerNotFoundException("Customer not found."));

            mockMvc.perform(post("/api/v1/customers/{id}/parcels", 123L)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsBytes(parcelRequestDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        public void customersCanOnlyAddParcelsFromTheirProfile() throws Exception {
            ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto();
            CourierUserResponseDto courierUserResponseDto = UserUtils.getUserCustomerResponseDto();

            CustomerResponseDto customerResponseDto = CustomerResponseDto.builder().courierUser(courierUserResponseDto).build();
            when(customerService.getCustomer(33L)).thenReturn(customerResponseDto);


            mockMvc.perform(post("/api/v1/customers/{id}/parcels", 33L)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsBytes(parcelRequestDto)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }
    }

    @DisplayName("Get a parcel resource")
    @WithMockUser(roles = "ADMIN", username = ParcelControllerTest.EMAIL)
    @Test
    public void getParcel() throws Exception {

        String id = UUID.randomUUID().toString();

        ParcelResponseDto parcelResponseDto = ParcelUtils.getParcelResponseDto(CustomerUtils.getCustomerResponseDto());
        parcelResponseDto.getSender().getCourierUser().setEmail(ParcelControllerTest.EMAIL);

        when(parcelService.getParcel(id)).thenReturn(parcelResponseDto);

        mockMvc.perform(get("/api/v1/parcels/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @DisplayName("Get a parcel resource that does not exist")
    @Test
    public void getParcelDoesNotExist() throws Exception {

        String id = UUID.randomUUID().toString();

        when(parcelService.getParcel(id)).thenThrow(new ParcelNotFoundException("Cannot find parcel of id " +id));

        mockMvc.perform(get("/api/v1/parcels/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get all parcels resource collection")
    @WithMockUser(roles = "ADMIN")
    @Test
    public void getAllParcels() throws Exception {

        List<ParcelResponseDto> parcelResponseDtoList = Arrays.asList(
                ParcelUtils.getParcelResponseDto(CustomerUtils.getCustomerResponseDto()),
                ParcelUtils.getParcelResponseDto(CustomerUtils.getCustomerResponseDto())
        );

        when(parcelService.getAllParcels(null, null)).thenReturn(parcelResponseDtoList);
        mockMvc.perform(get("/api/v1/parcels")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("The updating of the parcel")
    public void updateParcel() throws Exception {
        ParcelRequestDto parcelRequestDto = ParcelRequestDto.builder().status(ParcelStatus.IN_TRANSIT).build();

        mockMvc.perform(patch("/api/v1/parcels/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(parcelRequestDto)))
                .andExpect(status().isNoContent());

    }


}
