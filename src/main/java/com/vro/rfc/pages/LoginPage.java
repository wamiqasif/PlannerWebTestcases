package com.vro.rfc.pages;

import com.vro.rfc.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    /** Landing page (qa.valueresearch.in/login) defaults to OTP login - this switches to password login. */
    @FindBy(css = "button[data-user='Log in with password']")
    private WebElement loginWithPasswordButton;

    @FindBy(id = "username")
    private WebElement usernameInput;

    /** Appears only after the username step; advances to the password step. */
    @FindBy(id = "proceed-btn")
    private WebElement proceedButton;

    @FindBy(id = "login_password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@id='login-btn']")
    private WebElement submitButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void clickLoginWithPassword() {
        wait.waitForClickable(loginWithPasswordButton).click();
    }

    public void enterUsername(String username) {
        wait.waitForVisible(usernameInput).sendKeys(username);
    }

    public void clickProceed() {
        wait.waitForClickable(proceedButton).click();
    }

    public void enterPassword(String password) {
        wait.waitForVisible(passwordInput).sendKeys(password);
    }

    public void clickLogin() {
        wait.waitForClickable(submitButton).click();
    }

    /** Logs in with credentials resolved from config/environment (ConfigReader). */
    public void login() {
        login(ConfigReader.getUsername(), ConfigReader.getPassword());
    }

    public void login(String username, String password) {
        logger.info("Login started");
        clickLoginWithPassword();
        enterUsername(username);
        clickProceed();
        enterPassword(password);
        clickLogin();
    }
}
