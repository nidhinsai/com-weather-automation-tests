package com.weather.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Provides WebDriver instances based on the specified browser type and headless flag.
 */
public class WebDriverProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverProvider.class);

    private final WebDriver driver;

    /**
     * Initializes a WebDriver instance based on the specified browser type.
     *
     * @param browser    The type of browser (e.g., "chrome", "firefox").
     * @param headless   Whether to run in headless mode.
     */
    public WebDriverProvider(String browser, boolean headless) {
        LOGGER.info("Initializing {} driver (headless={})", browser, headless);
        this.driver = initializeDriver(browser, headless);
        configureDriver(this.driver);
    }

    private WebDriver initializeDriver(String browser, boolean headless) {
        switch (browser.toLowerCase()) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--window-size=1920,1080");
                }
                options.addArguments("--disable-extensions");
                options.addArguments("--remote-allow-origins=*");
                return new ChromeDriver(options);
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("--headless");
                }
                return new FirefoxDriver(options);
            }
            case "safari" -> {
                WebDriverManager.safaridriver().setup();
                return new SafariDriver();
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
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
            LOGGER.info("Quitting driver");
            driver.quit();
        }
    }
}
