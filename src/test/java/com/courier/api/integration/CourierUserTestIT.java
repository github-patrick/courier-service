package com.courier.api.integration;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.enums.UserType;
import com.courier.exception.ErrorApi;
import com.courier.utils.UserUtils;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("courier-user")
public class CourierUserTestIT extends BaseTest {

    public CourierUserTestIT() {
        requestSpecBuilder = new RequestSpecBuilder();
    }

    @Test
    public void shouldCreateCourier() {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);

        this.requestSpecBuilder.setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBody(courierUserRequestDto);

        Response response = given()
                .spec(requestSpecBuilder.build())
                .when()
                .post("courier-users")
                .then()
                .statusCode(201).extract().response();

        CourierUserResponseDto courierUserResponseDto = response.as(CourierUserResponseDto.class);
        assertEquals(courierUserRequestDto.getEmail(), courierUserResponseDto.getEmail());
    }

    @Test
    public void shouldNotCreateDuplicate() {
        CourierUserResponseDto courierUserResponseDto = UserUtils.createCourierUser(UserType.CUSTOMER);
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        courierUserRequestDto.setEmail(courierUserResponseDto.getEmail());

        requestSpecBuilder.setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBody(courierUserRequestDto);

        Response response = given()
                .spec(requestSpecBuilder.build())
                .when()
                .post("courier-users")
                .then()
                .statusCode(400).extract().response();

        ErrorApi errorApi = response.as(ErrorApi.class);
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorApi.getCode());
    }

    @Test
    public void shouldCreateUserViaXML() {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);

        requestSpecBuilder.setAccept(ContentType.XML)
                .setContentType(ContentType.XML)
                .setBody(courierUserRequestDto);

        Response response = given()
                .spec(requestSpecBuilder.build())
                .when()
                .post("courier-users")
                .then()
                .statusCode(201).extract().response();

        CourierUserResponseDto courierUserResponseDto = response.as(CourierUserResponseDto.class);
        assertEquals(courierUserRequestDto.getEmail(), courierUserResponseDto.getEmail());
    }

    @Test
    public void shouldNotCreateCourierInvalidEmail() {
        CourierUserRequestDto courierUserRequestDto = UserUtils.getUser(UserType.CUSTOMER);
        courierUserRequestDto.setEmail("dec-google.com");

        requestSpecBuilder.setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBody(courierUserRequestDto);

        Response response = given()
                .spec(requestSpecBuilder.build())
                .when()
                .post("courier-users")
                .then().statusCode(400)
                .extract().response();

        ErrorApi errorApi = response.as(ErrorApi.class);
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorApi.getCode());
    }
}
