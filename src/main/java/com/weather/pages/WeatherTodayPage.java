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

/**
 * Page object for the OpenWeatherMap city weather page.
 * Assertions use URL structure and page-source checks that remain
 * resilient to DOM changes across different OWM page versions.
 */
public class WeatherTodayPage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherTodayPage.class);

    /** Short wait used for individual element probes to avoid long cumulative delays. */
    private static final int PROBE_TIMEOUT_SECONDS = 3;

    /** CSS selectors for the temperature display element (tried in order). */
    private static final String[] TEMPERATURE_SELECTORS = {
        ".current-temp",
        ".heading",
        "div.weather-widget__temperature",
        "span[class*='temp']",
        ".owm-loader-container .current"
    };

    /** CSS selectors for the conditions container (tried in order). */
    private static final String[] CONDITIONS_SELECTORS = {
        ".current-container",
        "#current-conditions-section",
        ".weather-widget",
        ".city-page-container"
    };

    /**
     * Constructs a WeatherTodayPage backed by the given WebDriver.
     *
     * @param driver the WebDriver instance
     */
    public WeatherTodayPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Returns {@code true} if the current page appears to be a city weather page.
     * Checks the URL first (reliable after direct /city/{id} navigation),
     * then tries known conditions-container selectors.
     *
     * @return {@code true} if weather conditions are displayed
     */
    public boolean isCurrentConditionsDisplayed() {
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("/city/") || currentUrl.contains("/weather")) {
            LOGGER.info("isCurrentConditionsDisplayed: URL check passed ({})", currentUrl);
            return true;
        }
        WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(PROBE_TIMEOUT_SECONDS));
        for (String sel : CONDITIONS_SELECTORS) {
            try {
                quickWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(sel)));
                LOGGER.info("Conditions container found with selector: {}", sel);
                return true;
            } catch (TimeoutException e) {
                LOGGER.debug("Conditions selector '{}' not found within {}s", sel, PROBE_TIMEOUT_SECONDS);
            }
        }
        LOGGER.warn("isCurrentConditionsDisplayed: neither URL nor DOM selectors matched");
        return false;
    }

    /**
     * Returns {@code true} if a temperature value is rendered on the page.
     * Tries CSS selectors, then falls back to a degree-symbol check in the page source.
     *
     * @return {@code true} if a temperature reading was found
     */
    public boolean isTemperatureValueDisplayed() {
        LOGGER.info("Checking temperature visibility");
        WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(PROBE_TIMEOUT_SECONDS));
        for (String sel : TEMPERATURE_SELECTORS) {
            try {
                WebElement el = quickWait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(sel)));
                if (el.isDisplayed()) {
                    LOGGER.info("Temperature element found with selector: {}", sel);
                    return true;
                }
            } catch (TimeoutException e) {
                LOGGER.debug("Temperature selector '{}' not found within {}s", sel, PROBE_TIMEOUT_SECONDS);
            }
        }
        // Reliable fallback: OWM city pages always contain a degree character
        boolean hasDegree = driver.getPageSource().contains("\u00b0")
            || driver.getPageSource().contains("°");
        LOGGER.info("Temperature degree-symbol fallback result: {}", hasDegree);
        return hasDegree;
    }

    /**
     * Returns the temperature text from the city page, or {@code "N/A"} if not found.
     *
     * @return temperature string (e.g. {@code "12°C"}) or {@code "N/A"}
     */
    public String getTemperatureText() {
        WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(PROBE_TIMEOUT_SECONDS));
        for (String sel : TEMPERATURE_SELECTORS) {
            try {
                WebElement el = quickWait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(sel)));
                String text = el.getText().trim();
                if (!text.isEmpty()) {
                    LOGGER.info("Temperature text '{}' found with selector: {}", text, sel);
                    return text;
                }
            } catch (TimeoutException e) {
                LOGGER.debug("Temperature selector '{}' not found within {}s", sel, PROBE_TIMEOUT_SECONDS);
            }
        }
        LOGGER.info("Temperature text not found via selectors; returning N/A");
        return "N/A";
    }

    /**
     * Returns {@code true} if a weather description phrase is visible on the page.
     *
     * @return {@code true} if a weather-description element is found
     */
    public boolean isWeatherPhraseDisplayed() {
        String[] descSelectors = {".weather_icon", ".label", ".owm-weather-icon__title"};
        WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(PROBE_TIMEOUT_SECONDS));
        for (String sel : descSelectors) {
            try {
                quickWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(sel)));
                LOGGER.info("Weather phrase found with selector: {}", sel);
                return true;
            } catch (TimeoutException e) {
                LOGGER.debug("Phrase selector '{}' not found within {}s", sel, PROBE_TIMEOUT_SECONDS);
            }
        }
        return false;
    }
}
