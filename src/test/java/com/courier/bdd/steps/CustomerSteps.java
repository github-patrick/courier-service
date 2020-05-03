package com.courier.bdd.steps;

import com.courier.bdd.Context;
import com.courier.domain.dtos.CustomerRequestDto;
import com.courier.domain.dtos.CustomerResponseDto;
import com.courier.service.CustomerServiceImpl;
import com.courier.utils.CustomerUtils;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerSteps {

    private CustomerRequestDto customerRequestDto;

    @Autowired
    private Context context;

    @Autowired
    private CustomerServiceImpl customerService;

    @DataTableType
    public CustomerRequestDto customerEntry(Map<String, String> customer) {
        return new CustomerRequestDto(customer.get("fullName"), null, null);
    }

    @Given("I have a customer profile to create")
    public void i_have_a_customer_profile_to_create(List<CustomerRequestDto> customers) {
        customerRequestDto = customers.stream().findFirst().get();
    }

    @Given("I have a customer profile")
    public void i_have_a_customer_profile() throws Exception {
        CustomerRequestDto customerRequestDto = CustomerUtils.getCustomerRequestDto();
        CustomerResponseDto customerResponseDto = customerService.addCustomer(customerRequestDto, context.getCourierUser());
        context.setCustomer(customerResponseDto);
    }

    @When("I create the customer profile")
    public void i_create_the_customer_profile() {
        try {
            context.setCustomer(customerService.addCustomer(customerRequestDto, context.getCourierUser()));
        } catch (Exception ex) {}
    }

    @Then("the customer profile should be created in the system")
    public void the_customer_profile_should_be_created_in_the_system() {
        long count = customerService.getAllCustomers().stream().count();
        assertEquals(1,count);
    }

    @Then("associated to the correct user")
    public void associated_to_the_correct_user() {
        assertNotNull(customerService.getAllCustomers().stream().findFirst().get().getCourierUser());
    }

    @Then("the customer profile should not be created in the system")
    public void the_customer_profile_should_not_be_created_in_the_system() {
        long count = customerService.getAllCustomers().stream().count();
        assertEquals(0,count);
    }

    @Then("there should be only one customer profile for that user with the email address of {string}")
    public void there_should_be_only_one_customer_profile_for_that_user_with_the_email_address_of(String email) {

        long count = customerService.getAllCustomers().stream().filter(customerResponseDto ->
                customerResponseDto.getCourierUser().getEmail().equals(email)).count();
        assertEquals(1,count);

    }
}
