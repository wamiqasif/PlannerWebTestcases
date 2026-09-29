package com.vro.rfc.utils;

import com.vro.rfc.constants.FrameworkConstants;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    /**
     * Saves a screenshot as RFC001_verifyPortfolioWorkflow_20260928_143000.png
     * and returns its path, or null if the driver can't be screenshotted.
     */
    public static String capture(WebDriver driver, String rfcId, String testName) throws IOException {
        if (!(driver instanceof TakesScreenshot)) {
            return null;
        }

        File dir = new File(FrameworkConstants.SCREENSHOT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = String.format("%s_%s_%s.png", rfcId, testName, timestamp);

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(dir, fileName);
        FileUtils.copyFile(src, dest);
        return dest.getPath();
    }
}
