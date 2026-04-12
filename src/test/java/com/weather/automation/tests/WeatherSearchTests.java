package com.weather.automation.tests;

import com.weather.utils.TestDataUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test class for OpenWeatherMap location search and current-conditions verification.
 * Uses a DataProvider to run the same test against multiple cities.
 */
public class WeatherSearchTests extends BaseTest {

    /**
     * Provides city names from YAML test data for data-driven tests.
     * Falls back to Amsterdam if no data is configured.
     *
     * @return 2D array of city name parameters
     */
    @DataProvider(name = "cities")
    public Object[][] citiesDataProvider() {
        java.util.List<String> cities = TestDataUtils.getCities();
        if (cities == null || cities.isEmpty()) {
            return new Object[][]{{"Amsterdam"}};
        }
        return cities.stream()
                .map(c -> new Object[]{c})
                .toArray(Object[][]::new);
    }

    /**
     * Verifies that searching for a city navigates to a page that shows current
     * weather conditions and a temperature value.
     *
     * @param city the city name to search
     */
    @Test(dataProvider = "cities",
          description = "Search for a city and verify today's weather conditions are displayed")
    public void test_searchCityAndCheckTemperatureToday(String city) {
        weatherTodayPage = weatherHomePage.inputLocation(city);

        Assert.assertTrue(
                weatherTodayPage.isCurrentConditionsDisplayed(),
                "Current conditions not displayed for city: " + city);

        Assert.assertTrue(
                weatherTodayPage.isTemperatureValueDisplayed(),
                "Temperature value not displayed for city: " + city);

        String temperature = weatherTodayPage.getTemperatureText();
        Assert.assertNotNull(temperature,
                "Temperature text should not be null for city: " + city);
    }

    /**
     * Verifies the OpenWeatherMap page title contains weather-related content
     * after the initial navigation to the base URL.
     */
    @Test(description = "Verify page title contains weather-related content")
    public void test_pageTitleIsCorrect() {
        String title = getPageTitle();
        Assert.assertNotNull(title, "Page title should not be null");
        String lc = title.toLowerCase();
        Assert.assertTrue(
                lc.contains("weather") || lc.contains("forecast") || lc.contains("openweather"),
                "Page title should contain weather-related text, but was: " + title);
    }

    /**
     * Verifies that searching for a well-known city results in a URL containing
     * the /city/ path segment, confirming a successful city-page navigation.
     *
     * @param city the city name to search
     */
    @Test(dataProvider = "cities",
          description = "Verify city page URL after search navigation")
    public void test_cityPageUrlContainsCitySegment(String city) {
        weatherTodayPage = weatherHomePage.inputLocation(city);
        String currentUrl = getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("/city/") || currentUrl.contains("/weather"),
                "Expected URL to contain /city/ or /weather after navigating to " + city
                        + " but URL was: " + currentUrl);
    }
}
