package com.weather.automation.listeners;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TestListener class is responsible for capturing screenshots on test failure.
 */
public class TestListener implements ITestListener {

    /**
     * Method to capture screenshot on test failure.
     *
     * @param result The test result object.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = (WebDriver) result.getTestContext().getAttribute("WebDriver");
        String methodName = result.getMethod().getMethodName();
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        String fileNameOut = "screenshots/" + methodName + "_" + currentTime + ".png";
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File src = screenshot.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(src, new File(fileNameOut));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
