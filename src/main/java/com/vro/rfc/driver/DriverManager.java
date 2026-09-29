package com.vro.rfc.driver;

import com.vro.rfc.config.ConfigReader;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Centralized, single-instance WebDriver lifecycle for the whole suite.
 * initializeDriver() is called exactly once (from BaseTest's @BeforeSuite) and
 * quitDriver() exactly once (from @AfterSuite). No page object or test should
 * ever construct a WebDriver directly.
 */
public final class DriverManager {

    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static WebDriver driver;

    private DriverManager() {
    }

    public static synchronized void initializeDriver() {
        if (driver != null) {
            logger.warn("initializeDriver() called but a driver already exists - reusing it");
            return;
        }

        String browser = ConfigReader.getBrowser();
        boolean headless = ConfigReader.isHeadless();

        driver = createDriver(browser, headless);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        driver.manage().window().maximize();

        logger.info("Browser initialized: {} (headless={})", browser, headless);
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized - call initializeDriver() first");
        }
        return driver;
    }

    public static synchronized void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            logger.info("Browser closed");
        }
    }

    private static WebDriver createDriver(String browser, boolean headless) {
        switch (browser.toLowerCase()) {
            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                return new FirefoxDriver(options);
            }
            case "edge":
            case "msedge": {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new", "--disable-gpu");
                }
                options.addArguments("--window-size=1920,1080");
                return new EdgeDriver(options);
            }
            case "chrome":
            default: {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new", "--disable-gpu");
                }
                options.addArguments("--window-size=1920,1080", "--no-sandbox", "--disable-dev-shm-usage");
                options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
                return new ChromeDriver(options);
            }
        }
    }
}
