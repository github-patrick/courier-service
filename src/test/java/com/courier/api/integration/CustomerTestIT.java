package com.courier.api.integration;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerRequestDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.enums.UserType;
import com.courier.utils.CustomerUtils;
import com.courier.utils.UserUtils;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerIT extends BaseTest {



    @Test
    public void shouldCreateCustomerProfile() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.CUSTOMER);
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        customerRequestDto.setEmail(courierUserResponseDto.getEmail());

        final CustomerResponseDto customerResponseDto = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(customerRequestDto)
                .log().all()
                .when()
                .post("customers")
                .then()
                .log().all()
                .statusCode(201)
                .extract().as(CustomerResponseDto.class);

        assertNotNull(customerResponseDto.getId());
        assertTrue(customerResponseDto.getCourierUser().equals(courierUserResponseDto));
    }
}
