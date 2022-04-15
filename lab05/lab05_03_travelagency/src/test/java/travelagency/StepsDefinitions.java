package travelagency;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;


public class StepsDefinitions {

    private WebDriver driver;
    private JavascriptExecutor js;

    @Given("I am on the Travel Agency home page")
    public void atHomePage() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;

        driver.get("https://blazedemo.com/");
    }

    @When("I choose a flight from {string} to {string}")
    public void chooseFlight(String fromPort, String toPort) {
        driver.findElement(By.name("fromPort")).click();
        {
            WebElement dropdown = driver.findElement(By.name("fromPort"));
            dropdown.findElement(By.xpath("//option[. = '"+ fromPort +"']")).click();
        }

        driver.findElement(By.name("toPort")).click();
        {
            WebElement dropdown = driver.findElement(By.name("toPort"));
            dropdown.findElement(By.xpath("//option[. = '"+ toPort +"']")).click();
        }

    }

    @When("I click the {string} button")
    public void clickButton(String value) {
        driver.findElement(By.xpath("//input[@value='"+ value +"']")).click();
    }

    @And("I choose a flight from the list")
    public void iChooseAFlightFromTheList() {
        driver.findElement(By.cssSelector("tr:nth-child(1) .btn")).click();
    }


    @And("I fill the purchase form with valid information")
    public void iFillThePurchaseFormWithValidInformation() {
    }

    @Then("I should get the confirmation page")
    public void iShouldGetTheConfirmationPage() {
        assertThat(driver.getTitle(), containsStringIgnoringCase("BlazeDemo Confirmation"));
    }
}
