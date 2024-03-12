package pageObjects;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class MainPageContainer {

    public SelenideElement hamburgerButton = $(By.className("bm-burger-button"));
    public SelenideElement closeSidebarButton = $(By.id("react-burger-cross-btn"));

    private SelenideElement allItemsButton = $(By.id("inventory_sidebar_link"));
    private SelenideElement navigateToShoppingCartButton = $(By.className("shopping_cart_link"));

    public CartPage navigateToCartPage() {
        navigateToShoppingCartButton.click();
        return new CartPage();
    }
    public ProductsPage navigateToProductsPage() {
        hamburgerButton.click();
        allItemsButton.click();
        return new ProductsPage();
    }
}
