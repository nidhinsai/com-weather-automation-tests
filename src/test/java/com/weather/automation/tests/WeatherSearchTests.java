package com.weather.automation.tests;

import com.weather.utils.TestDataUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test class for weather.com location search and current conditions verification.
 */
public class WeatherSearchTests extends BaseTest {

    /**
     * Provides city names from YAML test data for data-driven tests.
     * Falls back to a default city if no data is configured.
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
     * Verifies that searching for a city shows the current conditions panel
     * with temperature and weather phrase displayed.
     *
     * @param city the city name to search
     */
    @Test(dataProvider = "cities", description = "Search for a city and verify today\'s weather conditions are displayed")
    public void test_searchCityAndCheckTemperatureToday(String city) {
        weatherTodayPage = weatherHomePage.inputLocation(city);
        Assert.assertTrue(weatherTodayPage.isCurrentConditionsDisplayed(),
                "Current conditions container not displayed for city: " + city);
        Assert.assertTrue(weatherTodayPage.isTemperatureValueDisplayed(),
                "Temperature value not displayed for city: " + city);
        String temperature = weatherTodayPage.getTemperatureText();
        Assert.assertNotNull(temperature, "Temperature text should not be null for city: " + city);
        Assert.assertFalse(temperature.isEmpty(), "Temperature text should not be empty for city: " + city);
    }

    /**
     * Verifies the weather.com page title contains recognisable text after loading.
     */
    @Test(description = "Verify page title contains weather-related content")
    public void test_pageTitleIsCorrect() {
        String title = getPageTitle();
        Assert.assertNotNull(title, "Page title should not be null");
        Assert.assertTrue(
                title.toLowerCase().contains("weather") || title.toLowerCase().contains("forecast"),
                "Page title should contain \'weather\' or \'forecast\', but was: " + title);
    }
}
