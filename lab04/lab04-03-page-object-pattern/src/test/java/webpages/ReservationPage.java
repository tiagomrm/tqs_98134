package webpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ReservationPage {

    private WebDriver driver;

    //Locators

    //Apply as Developer Button
    @FindBy(css = "tr:nth-child(1) .btn")
    private WebElement chooseThisFlightButton;

    //Constructor
    public ReservationPage(WebDriver driver){
        this.driver=driver;

        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened(){
        return driver.getTitle().equals("BlazeDemo - reserve");
    }

    public void clickOnChooseThisFlightButton(){
        chooseThisFlightButton.click();
    }

}
