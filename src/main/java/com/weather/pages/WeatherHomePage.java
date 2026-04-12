package com.weather.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * Page object for the OpenWeatherMap home/search flow.
 * Navigates to the /find?q= endpoint to avoid fragile form interaction,
 * then clicks the first matching city result.
 */
public class WeatherHomePage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherHomePage.class);

    private static final String BASE_URL = "https://openweathermap.org";

    /** CSS selectors tried in order to find a city result link. */
    private static final String[] RESULT_SELECTORS = {
        "ul.search-list li a",
        "a[href*='/city/']",
        ".widget-notification a",
        "table tbody tr td a"
    };

    /**
     * Constructor for WeatherHomePage.
     *
     * @param driver the WebDriver instance
     */
    public WeatherHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates directly to the OpenWeatherMap city-search results page for
     * the given location and clicks the first matching result.
     *
     * @param location the city name to look up
     * @return a {@link WeatherTodayPage} representing the city's weather page
     */
    public WeatherTodayPage inputLocation(String location) {
        LOGGER.info("Searching for location: {}", location);
        String encoded = URLEncoder.encode(location, StandardCharsets.UTF_8);
        String findUrl = BASE_URL + "/find?q=" + encoded;
        LOGGER.info("Navigating to search URL: {}", findUrl);
        driver.get(findUrl);

        WebElement firstResult = waitForFirstResult(location);
        LOGGER.info("Clicking first result for: {}", location);
        firstResult.click();
        return new WeatherTodayPage(driver);
    }

    /**
     * Waits for at least one city result link to appear on the search page.
     * Tries multiple CSS selectors in order, returning the first element found.
     *
     * @param location the searched location (for logging)
     * @return the first visible city result link
     * @throws RuntimeException if no result appears within the timeout
     */
    private WebElement waitForFirstResult(String location) {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        for (String selector : RESULT_SELECTORS) {
            try {
                List<WebElement> results = longWait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(selector)));
                if (!results.isEmpty()) {
                    LOGGER.info("Found {} result(s) for '{}' using selector: {}", results.size(), location, selector);
                    return results.get(0);
                }
            } catch (TimeoutException e) {
                LOGGER.debug("Selector '{}' did not produce results within timeout", selector);
            }
        }
        throw new RuntimeException(
            "No city search results found for '%s' on page: %s".formatted(location, driver.getCurrentUrl()));
    }
}
