package pageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProductsPage extends MainPageContainer{

    public SelenideElement header = $(By.className("header_label"));

    public ElementsCollection purchasableItemsList = $$(By.className("inventory_item"));
    public SelenideElement addBackpackToCartButton = $(By.id("add-to-cart-sauce-labs-backpack"));
    public SelenideElement addBikeLightsToCartButton = $(By.id("add-to-cart-sauce-labs-bike-light"));

    private SelenideElement sortProductsDropdown = $(By.className("product_sort_container"));


    public String getNameFromElement(SelenideElement element){
    return element.find(By.cssSelector(".inventory_item_name ")).getText();
    }
    public void changeSortingA_Z() {
        sortProductsDropdown
            .selectOption(0);
    }
    public void changeSortingZ_A() {
        sortProductsDropdown
            .selectOption(1);
    }
    public void changeSortingLow_High() {
        sortProductsDropdown
            .selectOption(2);
    }
    public void changeSortingHigh_Low() {
        sortProductsDropdown
            .selectOption(3);
    }
}
