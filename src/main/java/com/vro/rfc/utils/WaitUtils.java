package com.vro.rfc.utils;

import com.vro.rfc.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Thin wrapper around WebDriverWait (itself a FluentWait) for the UI conditions
 * RFC tests actually need. No Thread.sleep() anywhere in this framework.
 */
public final class WaitUtils {

    private final WebDriver driver;
    private final Duration timeout;

    public WaitUtils(WebDriver driver) {
        this(driver, ConfigReader.getExplicitWait());
    }

    public WaitUtils(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.timeout = Duration.ofSeconds(timeoutSeconds);
    }

    private WebDriverWait newWait() {
        return new WebDriverWait(driver, timeout);
    }

    public WebElement waitForVisible(By locator) {
        return newWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForVisible(WebElement element) {
        return newWait().until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForClickable(By locator) {
        return newWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForClickable(WebElement element) {
        return newWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public boolean waitForInvisible(By locator) {
        return newWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForText(By locator, String text) {
        return newWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public boolean waitForUrlContains(String fragment) {
        return newWait().until(ExpectedConditions.urlContains(fragment));
    }

    /**
     * Non-throwing presence check on a short custom timeout - used for session/state
     * recovery checks where "not there" is an expected, handled outcome rather than a
     * test failure.
     */
    public boolean isPresentWithin(By locator, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
