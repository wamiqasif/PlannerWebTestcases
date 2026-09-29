package com.vro.rfc.base;

import com.vro.rfc.config.ConfigReader;
import com.vro.rfc.driver.DriverManager;
import com.vro.rfc.listeners.TestListener;
import com.vro.rfc.pages.DashboardPage;
import com.vro.rfc.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Login Once -> Same Browser Session -> Execute All RFC Tests -> Cleanup Once.
 *
 * All RFC test classes extend this. @BeforeSuite/@AfterSuite here run exactly once
 * for the entire testng.xml suite (not per class) - see the comment in testng.xml
 * for why that only holds true when RFC test classes are listed inside ONE suite.
 *
 * Rules enforced by this class, do not violate them in RFC test classes:
 *  - No test class creates a WebDriver.
 *  - No test class calls login().
 *  - No test class calls driver.quit().
 */
public class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    protected static WebDriver driver;
    protected DashboardPage dashboardPage;

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        DriverManager.initializeDriver();
        driver = DriverManager.getDriver();

        driver.get(ConfigReader.getBaseUrl());

        new LoginPage(driver).login();

        dashboardPage = new DashboardPage(driver);
        dashboardPage.waitUntilLoaded();
        logger.info("LOGIN STATUS: SUCCESS");

        logger.info("Suite setup complete: browser launched once, logged in once, dashboard ready");
    }

    /**
     * Test isolation without re-login: before every test, make sure the session is
     * still authenticated and the app is back on the dashboard. Only re-authenticates
     * when the session has genuinely gone invalid - never unconditionally.
     */
    @BeforeMethod(alwaysRun = true)
    public void recoverToDashboard() {
        dashboardPage = new DashboardPage(driver);

        if (!dashboardPage.isLoaded()) {
            logger.warn("Session appears invalid before test - re-authenticating");
            driver.get(ConfigReader.getBaseUrl());
            new LoginPage(driver).login();
            dashboardPage = new DashboardPage(driver);
            dashboardPage.waitUntilLoaded();
            return;
        }

        if (!driver.getCurrentUrl().equals(ConfigReader.getBaseUrl())) {
            driver.get(ConfigReader.getBaseUrl());
            dashboardPage.waitUntilLoaded();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTearDown() {
        logger.info("Suite cleanup started");
        DriverManager.quitDriver();
    }

    /** Used by TestListener for screenshot-on-failure and current-URL reporting. */
    public static WebDriver getDriver() {
        return driver;
    }

    /**
     * Logs one execution step to BOTH the console (via SLF4J/Logback, under the
     * calling test class's own logger name) and the current test's entry in the
     * ExtentReports HTML report. Call this for every meaningful step in a test or
     * its helper methods, not just start/completed - that's what makes the report
     * a step-by-step trace instead of a bare pass/fail.
     */
    protected void logStep(String message) {
        LoggerFactory.getLogger(getClass()).info(message);
        TestListener.logStep(message);
    }
}
