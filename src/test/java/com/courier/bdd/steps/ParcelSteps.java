package com.courier.bdd.steps;

import com.courier.bdd.Context;
import com.courier.domain.dtos.ParcelRequestDto;
import com.courier.domain.enums.Priority;
import com.courier.service.ParcelService;
import com.courier.utils.ParcelUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class ParcelSteps {

    @Autowired
    private ParcelService parcelService;

    @Autowired
    private Context context;

    @Given("I have {int} parcels of priority {string}")
    public void i_have_parcels_of_priority(Integer number, String priority) {

        for (int i = 0; i < number ; i++) {
            ParcelRequestDto parcelRequestDto = ParcelUtils.getParcelRequestDto(Priority.valueOf(priority.toUpperCase()));
            parcelService.addParcel(parcelRequestDto, context.getCustomer());
        }
    }

    @When("I wait for {int} seconds")
    public void i_wait_for_seconds(Integer time) {
        try {
            Thread.sleep(time *1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
