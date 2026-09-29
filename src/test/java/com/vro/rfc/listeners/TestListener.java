package com.vro.rfc.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.vro.rfc.base.BaseTest;
import com.vro.rfc.constants.FrameworkConstants;
import com.vro.rfc.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Drives the ExtentReports HTML report and screenshot-on-failure. Registered once
 * via testng.xml's &lt;listeners&gt; - not via @Listeners on test classes.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    private static ExtentReports extent;
    private static ExtentTest currentTest;

    @Override
    public void onStart(ITestContext context) {
        File dir = new File(FrameworkConstants.REPORT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = dir.getPath() + File.separator + "RFC_Execution_Report_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @Override
    public void onTestStart(ITestResult result) {
        String rfcId = extractRfcId(result);
        currentTest = extent.createTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
        logger.info("{} started", rfcId);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String rfcId = extractRfcId(result);
        currentTest.log(Status.PASS, "Test passed");
        logger.info("{} completed", rfcId);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String rfcId = extractRfcId(result);
        logger.error("{} failed: {}", rfcId, result.getThrowable() != null ? result.getThrowable().getMessage() : "unknown error");

        WebDriver driver = BaseTest.getDriver();
        currentTest.log(Status.FAIL, result.getThrowable());

        if (driver != null) {
            try {
                String screenshotPath = ScreenshotUtils.capture(driver, rfcId, result.getMethod().getMethodName());
                if (screenshotPath != null) {
                    currentTest.addScreenCaptureFromPath(screenshotPath);
                }
            } catch (Exception e) {
                logger.error("Screenshot capture failed", e);
            }
            currentTest.info("Current URL: " + driver.getCurrentUrl());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    /**
     * Adds a step to the currently running test's report entry. Called from
     * BaseTest.logStep() so every test/page-object step shows up in the HTML
     * report, not just the final pass/fail status. Console logging is a separate
     * concern, handled by the SLF4J logger the caller passes through BaseTest.
     */
    public static void logStep(String message) {
        if (currentTest != null) {
            currentTest.log(Status.INFO, message);
        }
    }

    private String extractRfcId(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        int underscore = className.indexOf('_');
        return underscore > 0 ? className.substring(0, underscore) : className;
    }
}
