package com.courier.bdd;

import com.courier.CourierServiceApplication;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/SignUp.feature", glue = {"com.courier.bdd.steps",
        "com.courier.bdd.hook"}, plugin = "pretty")
public class RunCukesIT {
}
