package pageObjects;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class CheckoutInformationPage extends MainPageContainer{

    public SelenideElement firstNameField = $(By.id("first-name"));
    public SelenideElement lastNameField = $(By.id("last-name"));
    public SelenideElement postalCodeField = $(By.id("postal-code"));

    private SelenideElement continueButton = $(By.id("continue"));

    public CheckoutPage navigateToCheckoutPage() {
        continueButton.click();
        return new CheckoutPage();
    }

    public void fillInformationWithDefaultValues() {
        firstNameField
            .setValue("Thomas");
        lastNameField
            .setValue("Anderson");
        postalCodeField
            .setValue("12345");
    }

}
