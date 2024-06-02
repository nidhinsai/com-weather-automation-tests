package com.weather.automation.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WeatherSearchTests extends BaseTest {

    @Test()
    public void test_searchCityAndCheckTemperatureToday() {
        String city = "Amsterdam";
        openUrl("https://weather.com");
        weatherTodayPage = weatherHomePage.inputLocation(city);
        Assert.assertTrue(weatherTodayPage.isCurrentConditionsDisplayed(), "The current conditions for selected city is not displayed");
        Assert.assertTrue(weatherTodayPage.isTemperatureValueDisplayed(), "The temperature value for selected city is not displayed");
    }

}
