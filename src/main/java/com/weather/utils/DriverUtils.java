package com.weather.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * This class provides utility methods for managing the WebDriver instance.
 */
public class DriverUtils {

    private static final Logger logger = LoggerFactory.getLogger(DriverUtils.class);
    private static WebDriver driver;
    private static WebDriverWait wait;

    private DriverUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the WebDriver instance. If the instance is null, it initializes the driver.
     *
     * @return the WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    /**
     * Returns the WebDriverWait instance.
     *
     * @return the WebDriverWait instance
     */
    public static WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
        }
        return wait;
    }

    /**
     * Quits the WebDriver instance.
     */
    public static void quitDriver() {
        logger.info("Quitting the driver");
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    /**
     * Initializing the WebDriver instance.
     */
    private static void initializeDriver() {
        logger.info("Initializing web driver");
        System.setProperty("webdriver.chrome.driver", TestDataUtils.getDriverLocation());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        if (TestDataUtils.isHeadless()) {
            options.addArguments("--headless");
        }
        driver = new ChromeDriver(options);
        // driver.manage().window().setSize(new Dimension(1440, 900));
    }
}
