package pageObjects;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class SuccessPage extends MainPageContainer{

    public SelenideElement checkoutCompleteContainer = $(By.id("checkout_complete_container"));
    public SelenideElement completeHeader = checkoutCompleteContainer.$(By.className("complete-header"));

    private SelenideElement backHomeButton = checkoutCompleteContainer.$(By.id("back-to-products"));

    public ProductsPage navigateToProductsPage() {
        backHomeButton.click();
        return new ProductsPage();
    }
}
