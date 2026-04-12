package com.weather.automation.listeners;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TestNG listener that captures screenshots on test failure and logs test lifecycle events.
 */
public class TestListener implements ITestListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);
    private static final String SCREENSHOTS_DIR = "screenshots";

    /**
     * Invoked when a test method finishes but has failed.
     * Captures a screenshot and saves it to the screenshots directory.
     *
     * @param result the test result containing context
     */
    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.error("Test FAILED: {}", result.getMethod().getMethodName());
        WebDriver driver = (WebDriver) result.getTestContext().getAttribute("WebDriver");
        if (driver == null) {
            LOGGER.warn("WebDriver not available in context, skipping screenshot capture");
            return;
        }
        try {
            File screenshotsDir = new File(SCREENSHOTS_DIR);
            if (!screenshotsDir.exists() && !screenshotsDir.mkdirs()) {
                LOGGER.warn("Could not create screenshots directory: {}", SCREENSHOTS_DIR);
                return;
            }
            String methodName = result.getMethod().getMethodName();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String filePath = SCREENSHOTS_DIR + File.separator + methodName + "_" + timestamp + ".png";
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File src = screenshot.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(filePath));
            LOGGER.info("Screenshot saved: {}", filePath);
        } catch (IOException e) {
            LOGGER.error("Failed to capture screenshot for test: {}", result.getMethod().getMethodName(), e);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOGGER.info("Test STARTED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("Test PASSED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warn("Test SKIPPED: {}", result.getMethod().getMethodName());
    }
}
