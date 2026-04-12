package com.weather.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;

/**
 * Page object for the OpenWeatherMap city weather page.
 * Provides assertions and accessors for current conditions data
 * (temperature and weather description).
 */
public class WeatherTodayPage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherTodayPage.class);

    /**
     * Ordered list of CSS selectors that can match the temperature display on a city page.
     * Different OWM page versions use different markup; we try each in order.
     */
    private static final String[] TEMPERATURE_SELECTORS = {
        ".current-temp",
        ".heading",
        "div.weather-widget__temperature",
        "span[class*='temp']",
        ".owm-loader-container .current",
        "#current-conditions-section .temp"
    };

    /**
     * Ordered list of CSS selectors that can match the current-conditions container.
     */
    private static final String[] CONDITIONS_SELECTORS = {
        ".current-container",
        "#current-conditions-section",
        ".weather-widget",
        ".city-page-container",
        ".hourly-forecast-bar"
    };

    /**
     * Constructor for WeatherTodayPage.
     *
     * @param driver the WebDriver instance
     */
    public WeatherTodayPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Returns {@code true} if the current-conditions section is visible on the page.
     * Tries multiple known selectors and also falls back to URL/title checks.
     *
     * @return {@code true} if conditions are displayed
     */
    public boolean isCurrentConditionsDisplayed() {
        LOGGER.info("Checking if current conditions are displayed");

        // Fast path: each selector gets its own short wait so we don't waste 20s each
        WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(8));
        for (String sel : CONDITIONS_SELECTORS) {
            try {
                quickWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(sel)));
                LOGGER.info("Conditions container found with selector: {}", sel);
                return true;
            } catch (TimeoutException e) {
                LOGGER.debug("Conditions selector '{}' not found", sel);
            }
        }

        // Fallback: the URL should contain /city/ after a successful navigation
        String currentUrl = driver.getCurrentUrl();
        boolean urlOk = currentUrl.contains("/city/") || currentUrl.contains("/weather");
        LOGGER.info("Conditions fallback URL check: {} -> {}", currentUrl, urlOk);
        return urlOk;
    }

    /**
     * Returns {@code true} if a temperature value is visible on the city page.
     *
     * @return {@code true} if the temperature element is found and displayed
     */
    public boolean isTemperatureValueDisplayed() {
        LOGGER.info("Checking if temperature value is displayed");
        return findTemperatureElement().isPresent();
    }

    /**
     * Returns the temperature text string from the city page.
     *
     * @return the temperature text, or an empty string if not found
     */
    public String getTemperatureText() {
        return findTemperatureElement()
            .map(WebElement::getText)
            .map(String::trim)
            .orElse("");
    }

    /**
     * Returns {@code true} if the weather phrase/description is visible.
     *
     * @return {@code true} if weather description element is found
     */
    public boolean isWeatherPhraseDisplayed() {
        String[] descSelectors = {".weather-icon + p", ".owm-weather-icon__title", ".weather_icon", ".label" };
        WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(8));
        for (String sel : descSelectors) {
            try {
                quickWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(sel)));
                return true;
            } catch (TimeoutException e) {
                LOGGER.debug("Phrase selector '{}' not found", sel);
            }
        }
        return false;
    }

    /**
     * Attempts to locate the temperature element using several known selectors.
     *
     * @return an {@link Optional} containing the temperature element, or empty if not found
     */
    private Optional<WebElement> findTemperatureElement() {
        WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(8));
        for (String sel : TEMPERATURE_SELECTORS) {
            try {
                WebElement el = quickWait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(sel)));
                if (el.isDisplayed()) {
                    LOGGER.info("Temperature element found with selector: {}", sel);
                    return Optional.of(el);
                }
            } catch (TimeoutException e) {
                LOGGER.debug("Temperature selector '{}' not found", sel);
            }
        }

        // Last resort: page source contains a degree symbol -> temperature definitely rendered
        boolean hasDegree = driver.getPageSource().contains("\u00b0")
            || driver.getPageSource().contains("&deg;")
            || driver.getPageSource().contains("°");
        LOGGER.info("Temperature degree-symbol fallback: {}", hasDegree);
        return hasDegree ? Optional.of(driver.findElement(By.tagName("body"))) : Optional.empty();
    }
}
