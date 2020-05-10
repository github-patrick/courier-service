package com.courier.api.integration;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerRequestDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.enums.UserType;
import com.courier.exception.ErrorApi;
import com.courier.utils.CustomerUtils;
import com.courier.utils.UserUtils;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerTestIT extends BaseTest {

    @Test
    public void shouldCreateCustomerProfile() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.CUSTOMER);
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        customerRequestDto.setEmail(courierUserResponseDto.getEmail());

        ValidatableResponse response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(courierUserResponseDto.getEmail(),UserUtils.TEST_DEFAULT_PASSWORD)
                .body(customerRequestDto)
                .log().all()
                .when()
                .post("customers")
                .then()
                .log().all()
                .statusCode(201);

        CustomerResponseDto customerResponseDto = response.extract().as(CustomerResponseDto.class);

        assertNotNull(customerResponseDto.getId());
        assertTrue(customerResponseDto.getCourierUser().equals(courierUserResponseDto));
    }

    @Test
    public void shouldCreateCustomerProfileViaXml() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.CUSTOMER);
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        customerRequestDto.setEmail(courierUserResponseDto.getEmail());

        ValidatableResponse response = given()
                .accept(ContentType.XML)
                .contentType(ContentType.XML)
                .auth().preemptive().basic(courierUserResponseDto.getEmail(),UserUtils.TEST_DEFAULT_PASSWORD)
                .body(customerRequestDto)
                .log().all()
                .when()
                .post("customers")
                .then()
                .log().all()
                .statusCode(201);

        CustomerResponseDto customerResponseDto = response.extract().as(CustomerResponseDto.class);

        assertNotNull(customerResponseDto.getId());
        assertTrue(customerResponseDto.getCourierUser().equals(courierUserResponseDto));
    }

    @Test
    public void shouldNotCreateCustomerProfileWithNonExistentEmail() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.CUSTOMER);
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        customerRequestDto.setEmail("doesnot@exists.com");

        ValidatableResponse response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(courierUserResponseDto.getEmail(),UserUtils.TEST_DEFAULT_PASSWORD)
                .body(customerRequestDto)
                .log().all()
                .when()
                .post("customers")
                .then()
                .log().all()
                .statusCode(400);

        ErrorApi errorApi = response.extract().as(ErrorApi.class);

        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorApi.getCode());
    }

    @Test
    public void shouldNotDuplicateCustomerProfiles() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.CUSTOMER);
        CustomerResponseDto customerResponseDto = CustomerUtils.createCustomer(courierUserResponseDto);

        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        customerRequestDto.setEmail(customerResponseDto.getCourierUser().getEmail());

        ErrorApi errorApi = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(courierUserResponseDto.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)
                .body(customerRequestDto)
                .log().all().
                        when()
                .post("/customers")
                .then()
                .log().all()
                .extract().as(ErrorApi.class);

        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorApi.getCode());
    }
}
