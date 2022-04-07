package tests;


import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import webpages.ConfirmationPage;
import webpages.HomePage;
import webpages.PurchasePage;
import webpages.ReservationPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SeleniumJupiter.class)
public class TravelAgencyHtmlUnitTest {
  private WebDriver driver;
  JavascriptExecutor js;

  @BeforeEach
  public void setUp() {
    driver = new HtmlUnitDriver();
    js = (JavascriptExecutor) driver;
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }


  @Test
  public void purchaseConfirmationTest() {
    HomePage home = new HomePage(driver);

    // Click on Find Flights
    home.clickOnFindFlightsButton();

    ReservationPage reservation = new ReservationPage(driver);

    // Check if Reservation page is opened
    assertTrue(reservation.isPageOpened());

    // Click on Choose This Flight
    reservation.clickOnChooseThisFlightButton();

    PurchasePage purchase = new PurchasePage(driver);

    // Check if Purchase page is opened
    assertTrue(purchase.isPageOpened());

    // Click on Purchase Flight
    purchase.clickOnPurchaseFlightButton();

    ConfirmationPage confirmation = new ConfirmationPage(driver);

    // Check if Confirmation page is opened
    assertTrue(confirmation.isPageOpened());
  }
}
