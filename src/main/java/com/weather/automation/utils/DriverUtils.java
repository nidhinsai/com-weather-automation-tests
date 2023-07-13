package com.weather.automation.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

public class DriverUtils {

    private static final Logger logger = LoggerFactory.getLogger(DriverUtils.class);
    private static WebDriver driver;
    private static WebDriverWait wait;

    public static WebDriver getDriver() {
        if (driver == null) {
            try {
                TestDataUtils.loadConfigProperties();
                TestDataUtils.loadTestData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.setProperty("webdriver.chrome.driver", TestDataUtils.getDriverLocation());
            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized");
            if (TestDataUtils.isHeadless()) {
                options.addArguments("--headless");
            }
            driver = new ChromeDriver(options);
            // driver.manage().window().setSize(new Dimension(1440, 900));
        }
        return driver;
    }

    public static WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
        }
        return wait;
    }

    public static void quitDriver() {
        logger.info("Quiting the driver");
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
