package com.courier.bdd.steps;

import com.courier.bdd.Context;
import com.courier.domain.dtos.DeliveryDriverRequestDto;
import com.courier.domain.dtos.DeliveryDriverResponseDto;
import com.courier.domain.enums.DeliveryDriverStatus;
import com.courier.exception.CannotCreateDriverProfileException;
import com.courier.exception.DeliveryDriverNotFoundException;
import com.courier.repository.DeliveryDriverRepository;
import com.courier.service.DeliveryDriverServiceImpl;
import com.courier.utils.DeliveryDriverUtils;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DeliveryDriverSteps {

    @Autowired
    private Context context;

    private DeliveryDriverRequestDto deliveryDriverRequestDto;

    @Autowired
    private DeliveryDriverServiceImpl deliverDriverService;

    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;

    @DataTableType
    public DeliveryDriverRequestDto driverEntry(Map<String,String> drivers) {
        if (drivers.get("status")==null) {
            return new DeliveryDriverRequestDto(drivers.get("fullName"), null,
                    null);
        }
        return new DeliveryDriverRequestDto(drivers.get("fullName"), DeliveryDriverStatus.valueOf(drivers.get("status")),
                null);
    }


    @Given("I have a driver profile to create")
    public void i_have_a_driver_profile_to_create(List<DeliveryDriverRequestDto> drivers) {
        deliveryDriverRequestDto = drivers.stream().findFirst().get();
    }

    @Given("I have a driver profile")
    public void i_have_a_driver_profile() throws Exception {
        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverUtils.getDeliveryDriverRequestDto();
        DeliveryDriverResponseDto deliveryDriverResponseDto = deliverDriverService
                .addDeliveryDriver(deliveryDriverRequestDto, context.getCourierUser());

        context.setDeliveryDriver(deliveryDriverResponseDto);
    }

    @When("I create the driver profile")
    public void i_create_the_driver_profile() {
        try {
            context.setDeliveryDriver(deliverDriverService
                    .addDeliveryDriver(deliveryDriverRequestDto, context.getCourierUser()));
        } catch (Exception ex) {}
    }

    @When("I attempt to change my profile status to {string}")
    public void i_attempt_to_change_my_profile_status_to(String type) throws DeliveryDriverNotFoundException {
        DeliveryDriverRequestDto deliveryDriverRequestDto = DeliveryDriverRequestDto.builder().
                deliveryDriverStatus(DeliveryDriverStatus.valueOf(type.toUpperCase())).build();
        deliverDriverService.updateStatus(deliveryDriverRequestDto, context.getDeliveryDriver().getId());
    }

    @Then("the driver profile should be created in the system")
    public void the_driver_profile_should_be_created_in_the_system() {
        long count = deliverDriverService.getAllDeliveryDrivers().stream()
                .filter(driver -> driver.getCourierUser().equals(context.getCourierUser())).count();
        assertEquals(1, count);
    }

    @Then("the driver profile should not be created in the system")
    public void the_driver_profile_should_not_be_created_in_the_system() {
        long count = deliverDriverService.getAllDeliveryDrivers().stream()
                .filter(driver -> driver.getCourierUser().equals(context.getCourierUser())).count();
        assertEquals(0, count);
    }

    @Then("there should be only one driver profile for that user with the email address of {string}")
    public void there_should_be_only_one_driver_profile_for_that_user_with_the_email_address_of(String email) {
        long count = deliverDriverService.getAllDeliveryDrivers().stream()
                .filter(driver -> driver.getCourierUser().getEmail().equals(email)).count();
        assertEquals(1, count);
    }

    @Then("the driver profile status should be {string}")
    public void the_driver_profile_status_should_be(String type) {
        DeliveryDriverResponseDto deliveryDriverResponseDto = deliverDriverService.getAllDeliveryDrivers().stream()
                .filter(driver -> driver.getCourierUser().equals(context.getCourierUser())).findFirst().get();

        assertEquals(DeliveryDriverStatus.valueOf(type), deliveryDriverResponseDto.getDeliveryDriverStatus());
    }

    @Then("my new status should be {string}")
    public void my_new_status_should_be(String type) {
        DeliveryDriverResponseDto deliveryDriverResponseDto = deliverDriverService.getAllDeliveryDrivers().stream()
                .filter(driver -> driver.getCourierUser().equals(context.getCourierUser())).findFirst().get();

        assertEquals(DeliveryDriverStatus.valueOf(type), deliveryDriverResponseDto.getDeliveryDriverStatus());

    }



}
