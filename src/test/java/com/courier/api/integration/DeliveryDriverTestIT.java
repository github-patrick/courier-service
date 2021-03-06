package com.courier.api.integration;

import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.dtos.DeliveryDriverRequestDto;
import com.courier.domain.dtos.DeliveryDriverResponseDto;
import com.courier.domain.dtos.DeliveryDriverStatusDto;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.domain.enums.UserType;
import com.courier.exception.ErrorApi;
import com.courier.utils.DeliveryDriverUtils;
import com.courier.utils.UserUtils;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import javax.rmi.CORBA.Util;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("delivery-driver")
public class DeliveryDriverTestIT extends BaseTest {


    @Test
    public void shouldCreateDriverProfile() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.DRIVER);
        DeliveryDriverRequestDto deliveryDriverRequestDto =
                DeliveryDriverUtils.getDeliveryDriverRequestDto(courierUserResponseDto.getEmail());

        final DeliveryDriverResponseDto deliveryDriverResponseDto = given()
                .auth().preemptive().basic(courierUserResponseDto.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(deliveryDriverRequestDto)
                .when()
                .post("drivers")
                .then()
                .statusCode(201)
                .extract().as(DeliveryDriverResponseDto.class);

        assertNotNull(deliveryDriverResponseDto.getId());
    }

    @Test
    public void shouldNotCreateDriverProfileInvalidType() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.CUSTOMER);
        DeliveryDriverRequestDto deliveryDriverRequestDto =
                DeliveryDriverUtils.getDeliveryDriverRequestDto(courierUserResponseDto.getEmail());

        ResponseBodyExtractionOptions body = given()
                .auth().preemptive().basic(courierUserResponseDto.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(deliveryDriverRequestDto)
                .when()
                .post("drivers")
                .then()
                .statusCode(403).extract().body();

        String errorMessage = body.jsonPath().getString("error");
        assertEquals(HttpStatus.FORBIDDEN.getReasonPhrase(),errorMessage);
    }

    @Test
    public void shouldNotCreateDuplicateDriverProfile() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.DRIVER);
        DeliveryDriverResponseDto deliveryDriverResponseDto = DeliveryDriverUtils.createDeliveryDriver(courierUserResponseDto);

        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto(courierUserResponseDto.getEmail());

        final ErrorApi errorApi = given()
                .auth().preemptive().basic(courierUserResponseDto.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(deliveryDriverRequestDto)
                .when()
                .post("drivers")
                .then()
                .statusCode(400)
                .extract().as(ErrorApi.class);

        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorApi.getCode());
    }

    @Test
    public void shouldRetrieveAllDrivers() {

        CourierUserResponseDto courierUserOne = UserUtils.createCourierUser(UserType.ADMIN);
        CourierUserResponseDto courierUserTwo = UserUtils.createCourierUser(UserType.DRIVER);
        CourierUserResponseDto courierUserThree = UserUtils.createCourierUser(UserType.DRIVER);

        DeliveryDriverUtils.createDeliveryDriver(courierUserTwo);
        DeliveryDriverUtils.createDeliveryDriver(courierUserThree);

        DeliveryDriverResponseDto[] drivers = given()
                .accept(ContentType.JSON)
                .auth().preemptive().basic(courierUserOne.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)
                .queryParam("status","unavailable")
                .when()
                .get("drivers")
                .then()
                .statusCode(200)
                .extract().as(DeliveryDriverResponseDto[].class);

        assertEquals(2, Arrays.asList(drivers).size());
    }

    @Test
    public void shouldRetrieveDriver() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.DRIVER);
        DeliveryDriverResponseDto driverResponseDto = DeliveryDriverUtils.createDeliveryDriver(courierUserResponseDto);

        DeliveryDriverResponseDto deliveryDriverResponseDto = given()
                .accept(ContentType.JSON)
                .auth().preemptive().basic(courierUserResponseDto.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)
                .pathParam("id", driverResponseDto.getId())
                .when()
                .get("/drivers/{id}")
                .then()
                .extract().as(DeliveryDriverResponseDto.class);

        assertNotNull(deliveryDriverResponseDto.getId());
    }

    @Test
    public void shouldUpdateDriverStatus() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.DRIVER);
        DeliveryDriverResponseDto driverResponseDto = DeliveryDriverUtils.createDeliveryDriver(courierUserResponseDto);

        DeliveryDriverStatusDto deliveryDriverStatus =
                DeliveryDriverStatusDto.builder().deliveryDriverStatus(DeliveryDriverStatus.AVAILABLE).build();

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(courierUserResponseDto.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)
                .pathParam("id", driverResponseDto.getId())
                .body(deliveryDriverStatus)
        .when()
                .patch("/drivers/{id}")
        .then()
                .statusCode(204);

        DeliveryDriverResponseDto deliveryDriverResponseDto = given()
                .accept(ContentType.JSON)
                .auth().preemptive().basic(courierUserResponseDto.getEmail(), UserUtils.TEST_DEFAULT_PASSWORD)
                .pathParam("id", driverResponseDto.getId())
                .when()
                .get("/drivers/{id}")
                .then()
                .extract().as(DeliveryDriverResponseDto.class);

        assertEquals(DeliveryDriverStatus.AVAILABLE.name(), deliveryDriverResponseDto.getDeliveryDriverStatus().name());
    }
}
