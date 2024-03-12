package pageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage extends MainPageContainer{

    public ElementsCollection itemsInCartList = $$(By.className("cart_item"));

    private SelenideElement navigateToInformationPage = $(By.id("checkout"));


    public CheckoutInformationPage navigateToInformationPage() {
        navigateToInformationPage.click();
        return new CheckoutInformationPage();
    }

    public void removeElementFromCheckoutByIndex(int index) {
        itemsInCartList.get(index)
            .$(By.cssSelector(".btn.btn_secondary.btn_small.cart_button"))
            .click();
    }
}
