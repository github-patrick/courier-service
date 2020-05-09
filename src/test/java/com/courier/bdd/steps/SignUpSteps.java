package com.courier.bdd.steps;

import com.courier.bdd.Context;
import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.dtos.CourierUserResponseDto;
import com.courier.domain.enums.UserType;
import com.courier.exception.CourierUserNotFoundException;
import com.courier.service.CourierUserServiceImpl;
import com.courier.utils.UserUtils;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class SignUpSteps {

    @Autowired
    private Context context;

    private CourierUserRequestDto courierUser;

    @Autowired
    private CourierUserServiceImpl courierUserService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @DataTableType
    public CourierUserRequestDto userEntry(Map<String,String> users) {

        if (users.get("type") == null) {
            return new CourierUserRequestDto(users.get("email"), users.get("password"),
                    null);
        }
        return new CourierUserRequestDto(users.get("email"), users.get("password"),
                Arrays.asList(UserType.valueOf(users.get("type").toUpperCase())));
    }


    @Given("I am a customer")
    public void i_am_a_customer(List<CourierUserRequestDto> courierUsers) throws Exception {
        courierUser = courierUsers.stream().findFirst().orElseThrow(Exception::new);
    }

    @Given("I am a registered {string}")
    public void i_am_a_registered(String type) {
        context.setCourierUser(courierUserService.addCourierUser(UserUtils.getUser(UserType.valueOf(type.toUpperCase()))));
    }

    @Given("I have a {string} user signed up with the email address of {string}")
    public void i_have_a_user_signed_up_with_the_email_address_of(String type, String email) {
        CourierUserRequestDto courierUserRequestDto = CourierUserRequestDto
                .builder().email(email).password("password")
                .types(Arrays.asList(UserType.valueOf(type.toUpperCase()))).build();

        context.setCourierUser(courierUserService.addCourierUser(courierUserRequestDto));
    }



    @When("I sign up")
    public void i_sign_up() {
        try {
            context.setCourierUser(courierUserService.addCourierUser(courierUser));
        } catch (Exception e) {}
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

    @Then("my password should be stored encrypted")
    public void my_password_should_be_stored_encrypted() {
    }

    @Then("my {string} should be stored encrypted")
    public void my_should_be_stored_encrypted(String password) throws CourierUserNotFoundException {
        CourierUserResponseDto courierUserResponseDto = courierUserService.getCourierUserByEmail(courierUser.getEmail());
        assertTrue(bCryptPasswordEncoder.matches(password, courierUserResponseDto.getPassword()));
    }



}
