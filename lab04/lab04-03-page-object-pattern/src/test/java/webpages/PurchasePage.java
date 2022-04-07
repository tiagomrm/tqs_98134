package webpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class PurchasePage {

    private WebDriver driver;

    //Locators

    //Apply as Developer Button
    @FindBy(className = "btn-primary")
    private WebElement purchaseFlightButton;

    //Constructor
    public PurchasePage(WebDriver driver){
        this.driver=driver;

        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened(){
        return driver.getTitle().equals("BlazeDemo Purchase");
    }

    public void clickOnPurchaseFlightButton(){
        purchaseFlightButton.click();
    }

}
