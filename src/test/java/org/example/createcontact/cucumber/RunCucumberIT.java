package org.example.createcontact.cucumber;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources",
        glue="org/example/createcontact/steps",
        tags="@createcontact"
)

public class RunCucumberIT {

}
