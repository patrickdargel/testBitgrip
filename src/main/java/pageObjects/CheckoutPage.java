package pageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CheckoutPage extends MainPageContainer{

    public ElementsCollection itemsInCartList = $$(By.className("cart_item"));
    private SelenideElement finishButton = $(By.id("finish"));

    public SelenideElement getElementFromList(int index) {
        return itemsInCartList.get(index);
    }

    public SuccessPage navigateToSuccessPage() {
        finishButton.click();
        return new SuccessPage();
    }
}
