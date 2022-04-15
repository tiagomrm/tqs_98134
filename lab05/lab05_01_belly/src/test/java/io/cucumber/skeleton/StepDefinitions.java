package io.cucumber.skeleton;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefinitions {

    Belly belly;

    @Given("I have {int} cukes in my belly")
    public void I_have_cukes_in_my_belly(int cukes) {
        belly = new Belly();
        belly.eat(cukes);
    }

    @When("I wait {int} hour")
    public void iWaitHour(int hours) {
        belly.waitFor(hours);
    }

    @Then("my belly should growl")
    public void myBellyShouldGrowl() {
        assertTrue(belly.isGrowling());
    }
}
