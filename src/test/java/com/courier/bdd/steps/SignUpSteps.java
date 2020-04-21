package com.courier.bdd.steps;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.enums.UserType;
import com.courier.repository.CourierUserRepository;
import com.courier.service.CourierUserServiceImpl;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class SignUpSteps {

    private CourierUserRequestDto courierUser;

    @Autowired
    private CourierUserServiceImpl courierUserService;

    @Autowired
    private CourierUserRepository courierUserRepository;


    @DataTableType
    public CourierUserRequestDto userEntry(Map<String,String> users) {
        return new CourierUserRequestDto(users.get("email"), users.get("password"),
                UserType.valueOf(users.get("type").toUpperCase()));
    }


    @Given("I am a customer")
    public void i_am_a_customer(List<CourierUserRequestDto> courierUsers) throws Exception {
        courierUser = courierUsers.stream().findFirst().orElseThrow(Exception::new);
    }

    @Given("a user is signed up with the email address of {string}")
    public void a_user_is_signed_up_with_the_email_address_of(String email) {
        CourierUserRequestDto courierUserRequestDto = CourierUserRequestDto
                .builder().email(email).password("password").userType(UserType.CUSTOMER).build();
        courierUserService.addCourierUser(courierUserRequestDto);
    }



    @When("I sign up")
    public void i_sign_up() {
        try {
            courierUserService.addCourierUser(courierUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("I should be successfully registered as a customer")
    public void i_should_be_successfully_registered_as_a_customer() {
        CourierUserResponseDto courierUser = courierUserService.getAllCourierUsers().stream().findFirst().get();
        assertThat(courierUser.getEmail(), equalTo(courierUser.getEmail()));
    }

    @Then("I should not be successful in registration")
    public void i_should_not_be_successful_in_registration() {
        List<CourierUserResponseDto> courierUserResponseDtoList = courierUserService.getAllCourierUsers();
        assertSame(0, courierUserResponseDtoList.size());
    }

    @Then("I should not be successful in registration as the email is not unique")
    public void i_should_not_be_successful_in_registration_as_the_email_is_not_unique() {
        List<CourierUserResponseDto> courierUserResponseDtoList = courierUserService.getAllCourierUsers();
        long count = courierUserResponseDtoList.stream().filter(courierUser ->
                courierUser.getEmail().equals(this.courierUser.getEmail())).count();
        assertSame(1L, count);


    }



}
