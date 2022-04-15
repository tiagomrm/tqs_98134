package io.cucumber.skeleton;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorStepDefinitions {

    Calculator calc = new Calculator();

    @Given("a calculator I just turned on")
    public void aCalculatorIJustTurnedOn() {
        calc = new Calculator();
    }

    @When("I add {int} and {int}")
    public void iAddAnd(int arg0, int arg1) {
        calc.push(arg0);
        calc.push(arg1);
        calc.push("+");
    }

    @Then("the result is {int}")
    public void theResultIs(int expected) {
        assertEquals(expected, calc.value());
    }

    @When("I substract {int} to {int}")
    public void iSubstractTo(int arg0, int arg1) {
        calc.push(arg0);
        calc.push(arg1);
        calc.push("-");
    }
}
