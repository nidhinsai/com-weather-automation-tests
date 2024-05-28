package com.weather.automation.tests;

import org.testng.annotations.Test;

public class WeatherSearchTests extends BaseTest {

    @Test()
    public void test_sample() {
        openUrl("https://weather.com");
        weatherHomePage.inputLocation("Amsterdam");
    }

}
