package com.weather.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;

/**
 * Provides WebDriver instances based on the specified browser type using dependency injection.
 */
public class WebDriverProvider {
    private final WebDriver driver;

    /**
     * Initializes a WebDriver instance based on the specified browser type.
     *
     * @param browser The type of browser (e.g., "chrome", "firefox").
     */
    public WebDriverProvider(String browser) {
        this.driver = initializeDriver(browser);
        configureDriver(this.driver);
    }

    private WebDriver initializeDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            }
            case "safari" -> {
                WebDriverManager.safaridriver().setup();
                return new SafariDriver();
            }
            default -> throw new IllegalArgumentException("Invalid browser specified: " + browser);
        }
    }

    /**
     * Retrieves the initialized WebDriver instance.
     *
     * @return The WebDriver instance.
     */
    public WebDriver getDriver() {
        return driver;
    }

    private void configureDriver(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    /**
     * Quits the WebDriver instance.
     */
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
