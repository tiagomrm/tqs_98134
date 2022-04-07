package travelagency;


import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SeleniumJupiter.class)
public class TravelAgencyTest {
  private WebDriver driver;
  JavascriptExecutor js;

  @BeforeAll
  static void setUpClass() {
    WebDriverManager.chromedriver().setup();
  }

  @BeforeEach
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }


  @Test
  public void purchaseConfirmationTest() {
    driver.get("https://blazedemo.com/");
    driver.manage().window().setSize(new Dimension(1614, 1039));
    driver.findElement(By.cssSelector("h2:nth-child(3)")).click();
    driver.findElement(By.name("fromPort")).click();
    driver.findElement(By.name("fromPort")).click();
    driver.findElement(By.name("toPort")).click();
    driver.findElement(By.cssSelector(".btn-primary")).click();
    assertThat(driver.getTitle(), is("BlazeDemo - reserve"));
    assertThat(driver.findElement(By.cssSelector("h3")).getText(), is("Flights from Paris to Buenos Aires:"));
    driver.findElement(By.cssSelector("tr:nth-child(1) .btn")).click();
    driver.findElement(By.cssSelector(".btn-primary")).click();
    assertThat(driver.getTitle(), is("BlazeDemo Confirmation"));
  }
}
