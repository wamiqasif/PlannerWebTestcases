package com.vro.rfc.tests.rfc;

import com.vro.rfc.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Representative RFC test. Business logic and assertions only - all framework
 * setup (browser, login, session recovery) is handled by BaseTest.
 */
public class RFC001_Test extends BaseTest {

    @Test(description = "Verify RFC001 - authenticated user can navigate from Dashboard to Portfolio")
    public void verifyRFC001PortfolioNavigation() {
        logStep("RFC001 started");

        logStep("Navigating from Dashboard to Portfolio");
        dashboardPage.navigateToPortfolio();

        logStep("Asserting current URL contains 'portfolio'");
        Assert.assertTrue(
                dashboardPage.getCurrentUrl().toLowerCase().contains("portfolio"),
                "Expected URL to contain 'portfolio' after navigating from Dashboard");

        logStep("RFC001 completed");
    }
}
