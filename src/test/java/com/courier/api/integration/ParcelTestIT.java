package com.courier.api.integration;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.domain.dtos.ParcelRequestDto;
import com.courier.domain.dtos.ParcelResponseDto;
import com.courier.domain.enums.ParcelStatus;
import com.courier.domain.enums.Priority;
import com.courier.domain.enums.UserType;
import com.courier.utils.CustomerUtils;
import com.courier.utils.ParcelUtils;
import com.courier.utils.UserUtils;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParcelTestIT extends BaseTest{

    @DisplayName("Should add parcel for delivery")
    @Test
    public void addParcel() {

        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.CUSTOMER);
        CustomerResponseDto customerResponseDto = CustomerUtils.createCustomer(courierUserResponseDto);
        ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto();

        RequestSpecification requestSpecification = this.requestSpecBuilder.setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setBody(parcelRequestDto)
                .addPathParam("id", customerResponseDto.getId())
                .setAuth(basic(courierUserResponseDto.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)).build();

        ParcelResponseDto parcelResponseDto = given()
                .spec(requestSpecification)
                .log().all()
                .when()
                .post("customers/{id}/parcels")
                .then()
                .log().all()
                .statusCode(201).extract().as(ParcelResponseDto.class);

        assertEquals(Priority.MEDIUM,parcelResponseDto.getPriority());
        assertEquals(ParcelStatus.NOT_DISPATCHED,parcelResponseDto.getStatus());
    }

    @DisplayName("Should add parcel for delivery")
    @Test
    public void addParcelViaXml() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.CUSTOMER);
        CustomerResponseDto customerResponseDto = CustomerUtils.createCustomer(courierUserResponseDto);
        ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto(Priority.MEDIUM);

        RequestSpecification requestSpecification = this.requestSpecBuilder.setContentType(ContentType.XML)
                .setAccept(ContentType.XML)
                .setBody(parcelRequestDto)
                .addPathParam("id",customerResponseDto.getId())
                .setAuth(basic(courierUserResponseDto.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)).build();


        given()
                .spec(requestSpecification)
                .when()
                .post("customers/{id}/parcels")
                .then()
                .statusCode(201);
    }

    @Test
    public void getParcel() {
        ParcelResponseDto parcel = ParcelUtils.createAndGetParcel();
        CourierUserResponseDto courierUserResponseDto = parcel.getSender().getCourierUser();

        RequestSpecification requestSpecification = this.requestSpecBuilder
                .setAccept(ContentType.JSON)
                .addPathParam("id", parcel.getId())
                .setAuth(basic(courierUserResponseDto.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)).build();

        ParcelResponseDto parcelResponseDto = given()
                .spec(requestSpecification)
                .when()
                .get("parcels/{id}")
                .then()
                .statusCode(200)
                .extract().as(ParcelResponseDto.class);

        assertEquals(ParcelStatus.NOT_DISPATCHED, parcelResponseDto.getStatus());
    }

    @Test
    public void updateParcel() {

        ParcelResponseDto parcel = ParcelUtils.createAndGetParcel();
        CourierUserResponseDto courierUserResponseDto = parcel.getSender().getCourierUser();

        Map<String,String> httpRequestBody = new HashMap();
        httpRequestBody.put("status", "IN_TRANSIT");

        RequestSpecification requestSpecification = this.requestSpecBuilder
                .setAccept(ContentType.JSON)
                .addPathParam("id", parcel.getId())
                .setBody(httpRequestBody)
                .setAuth(basic(ADMIN_EMAIL, "password")).build();

        given()
                .spec(requestSpecification)
                .when()
                .patch("parcels/{id}")
                .then()
                .statusCode(204);
    }
}
