package pageObjects;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public SelenideElement userNameField = $(By.id("user-name"));
    public SelenideElement passwordField = $(By.id("password"));

    private SelenideElement loginButton = $(By.id("login-button"));

    public LoginPage open() {
        Selenide.open("/");
        return this;
    }

    public ProductsPage loginWithStandardUser() {
        userNameField.setValue("standard_user");
        passwordField.setValue("secret_sauce");
        loginButton.click();
        return new ProductsPage();
    }

}
