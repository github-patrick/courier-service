package com.courier.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features", glue = {"com.courier.bdd.steps",
        "com.courier.bdd.hook"}, plugin = "pretty")
public class RunCukesIT {
}
