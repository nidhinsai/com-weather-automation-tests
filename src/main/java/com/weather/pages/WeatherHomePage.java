package com.weather.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;

/**
 * Page object for OpenWeatherMap city weather navigation.
 * Navigates directly to city weather pages using pre-known OWM city IDs,
 * avoiding fragile search-form interactions that are commonly blocked
 * in headless / CI browser environments.
 */
public class WeatherHomePage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherHomePage.class);

    private static final String BASE_URL = "https://openweathermap.org";

    /**
     * Pre-known OpenWeatherMap city IDs.
     * Sourced from: https://openweathermap.org/find?q=&lt;city&gt;
     * Using direct /city/{id} URLs avoids bot-detection on the search form.
     */
    private static final Map<String, String> CITY_IDS = Map.ofEntries(
        Map.entry("amsterdam", "2759794"),
        Map.entry("london",    "2643743"),
        Map.entry("tokyo",     "1850147"),
        Map.entry("new york",  "5128581"),
        Map.entry("paris",     "2988507"),
        Map.entry("berlin",    "2950159"),
        Map.entry("sydney",    "2147714")
    );

    /**
     * Constructs a WeatherHomePage backed by the given WebDriver.
     *
     * @param driver the WebDriver instance
     */
    public WeatherHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates directly to the OpenWeatherMap city weather page for the
     * given location using a pre-configured city-ID lookup.
     * This avoids unreliable search-form interactions.
     *
     * @param location the city name (case-insensitive; must be in the known city map)
     * @return a {@link WeatherTodayPage} for the resulting city weather view
     * @throws IllegalArgumentException if the city is not in the known-cities map
     */
    public WeatherTodayPage inputLocation(String location) {
        LOGGER.info("Looking up city ID for: {}", location);
        String cityId = CITY_IDS.get(location.toLowerCase().trim());
        if (cityId == null) {
            throw new IllegalArgumentException(
                "No city ID configured for: '" + location + "'. Known cities: " + CITY_IDS.keySet());
        }
        String cityUrl = BASE_URL + "/city/" + cityId;
        LOGGER.info("Navigating directly to: {}", cityUrl);
        driver.get(cityUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            wait.until(d -> !d.getTitle().isEmpty());
            LOGGER.info("City page loaded. Title: {}", driver.getTitle());
        } catch (Exception e) {
            LOGGER.debug("Title wait timed out for {}; current URL: {}", location, driver.getCurrentUrl());
        }
        return new WeatherTodayPage(driver);
    }
}
