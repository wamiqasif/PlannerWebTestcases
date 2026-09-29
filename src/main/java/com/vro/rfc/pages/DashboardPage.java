package com.vro.rfc.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage {

    /** Only present once the authenticated app shell (nav bar) has loaded. */
    private static final String PORTFOLIO_LINK_XPATH = "//a[normalize-space()='Portfolio']";

    @FindBy(xpath = PORTFOLIO_LINK_XPATH)
    private WebElement portfolioNavLink;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilLoaded() {
        wait.waitForVisible(portfolioNavLink);
        logger.info("Dashboard loaded");
    }

    /** Short, non-throwing check - used for session/state recovery, not assertions. */
    public boolean isLoaded() {
        return wait.isPresentWithin(By.xpath(PORTFOLIO_LINK_XPATH), 5);
    }

    public void navigateToPortfolio() {
        wait.waitForClickable(portfolioNavLink).click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
