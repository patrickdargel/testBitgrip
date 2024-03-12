package frontend;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;

public class PurchaseTests {

    private static final Properties properties = new Properties();

    @BeforeAll
    public static void setup() {
        try {
            properties.load(new FileInputStream("src/test/resources/frontendResources.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Configuration.baseUrl = properties.getProperty("baseURI");

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
    }

    @Test
    public void userCanLoginByUsernameAndPassword() {
        LoginPage loginPage = new LoginPage().open();
        ProductsPage productsPage = loginPage.loginWithStandardUser();

        productsPage
            .header
            .shouldBe(visible);
    }

    @Test
    public void userCanSuccessfullyPurchaseBag() {
        LoginPage loginPage = new LoginPage().open();
        ProductsPage productsPage = loginPage.loginWithStandardUser();
        productsPage
            .addBackpackToCartButton
            .click();

        CartPage cartPage = productsPage.navigateToCartPage();
        cartPage
            .itemsInCartList
            .shouldHave(size(1));

        CheckoutInformationPage checkoutInformationPage = cartPage.navigateToInformationPage();
        checkoutInformationPage
            .firstNameField
            .setValue("Thomas");
        checkoutInformationPage
            .lastNameField
            .setValue("Anderson");
        checkoutInformationPage
            .postalCodeField
            .setValue("12345");

        CheckoutPage checkoutPage = checkoutInformationPage.navigateToCheckoutPage();
        checkoutPage
            .itemsInCartList
                .shouldHave(size(1));
        checkoutPage
            .getElementFromList(0)
            .shouldHave(text("Sauce Labs Backpack"));

        SuccessPage successPage = checkoutPage.navigateToSuccessPage();
        successPage
            .completeHeader
            .shouldHave(text("Thank you for your order!"));

        productsPage = successPage.navigateToProductsPage();
        productsPage
            .header
            .shouldBe(visible);
    }

    @Test
    public void userCanChangeTheSortingOnProductsPage() {
        LoginPage loginPage = new LoginPage().open();
        ProductsPage productsPage = loginPage.loginWithStandardUser();
        productsPage
            .getNameFromElement(
                productsPage
                    .purchasableItemsList
                    .first()
                    .shouldHave(text("Sauce Labs Backpack")
                    ));

        productsPage
            .changeSortingZ_A();
        productsPage
            .getNameFromElement(
                productsPage
                    .purchasableItemsList
                    .first()
                    .shouldHave(text("Test.allTheThings() T-Shirt (Red)")
                    ));

        productsPage
            .changeSortingLow_High();
        productsPage
            .getNameFromElement(
                productsPage
                    .purchasableItemsList
                    .first()
                    .shouldHave(text("Sauce Labs Onesie")
                    ));

        productsPage
            .changeSortingHigh_Low();
        productsPage
            .getNameFromElement(
                productsPage
                    .purchasableItemsList
                    .first()
                    .shouldHave(text("Sauce Labs Fleece Jacket")
                    ));

        productsPage
            .changeSortingA_Z();
        productsPage
            .getNameFromElement(
                productsPage
                    .purchasableItemsList
                    .first()
                    .shouldHave(text("Sauce Labs Backpack")
                    ));
    }

    @Test
    public void userCanChangeTheOrder() {
        LoginPage loginPage = new LoginPage().open();
        ProductsPage productsPage = loginPage.loginWithStandardUser();

        productsPage
            .addBackpackToCartButton
            .click();
        CartPage cartPage = productsPage
            .navigateToCartPage();
        cartPage
            .itemsInCartList
            .shouldHave(size(1));
        productsPage = cartPage
            .navigateToProductsPage();
        productsPage
            .addBikeLightsToCartButton
            .click();
        cartPage = productsPage
            .navigateToCartPage();
        cartPage
            .itemsInCartList
            .shouldHave(size(2));
        cartPage
            .removeElementFromCheckoutByIndex(0);
        cartPage
            .itemsInCartList
            .shouldHave(size(1));

        CheckoutInformationPage checkoutInformationPage = cartPage.navigateToInformationPage();
        checkoutInformationPage
            .fillInformationWithDefaultValues();

        CheckoutPage checkoutPage = checkoutInformationPage.navigateToCheckoutPage();
        checkoutPage
            .itemsInCartList
            .shouldHave(size(1));
        checkoutPage
            .getElementFromList(0)
            .shouldHave(text("Sauce Labs Bike Light"));

        SuccessPage successPage = checkoutPage.navigateToSuccessPage();
        successPage
            .completeHeader
            .shouldHave(text("Thank you for your order!"));

        productsPage = successPage.navigateToProductsPage();
        productsPage
            .header
            .shouldBe(visible);

    }
}
