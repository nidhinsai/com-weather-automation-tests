package com.weather.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Page object for the Weather.com home page.
 * Contains elements and methods for the location search functionality.
 */
public class WeatherHomePage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherHomePage.class);

    @FindBy(id = "LocationSearch_input")
    private WebElement locationSearchInput;

    @FindBy(id = "LocationSearch_listbox")
    private WebElement locationSearchListBox;

    /**
     * Constructor for WeatherHomePage.
     *
     * @param driver the WebDriver instance
     */
    public WeatherHomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Inputs a location in the search box and selects the matching city from the dropdown.
     *
     * @param location the city name to search for
     * @return a WeatherTodayPage after selecting the city
     */
    public WeatherTodayPage inputLocation(String location) {
        LOGGER.info("Searching for location: {}", location);
        waitForElementToBeClickable(locationSearchInput);
        locationSearchInput.click();
        locationSearchInput.sendKeys(location);
        waitForElementToBeVisible(locationSearchListBox);
        selectCity(location);
        return new WeatherTodayPage(driver);
    }

    /**
     * Selects the first city from the dropdown that matches the given city name.
     *
     * @param cityName the city name substring to match
     */
    public void selectCity(String cityName) {
        List<WebElement> cityButtons = locationSearchListBox.findElements(By.tagName("button"));
        if (cityButtons.isEmpty()) {
            LOGGER.warn("No city suggestions found for: {}", cityName);
            return;
        }
        for (WebElement cityButton : cityButtons) {
            String buttonText = cityButton.getText();
            if (buttonText.contains(cityName)) {
                LOGGER.info("Selecting city: {}", buttonText);
                waitForElementToBeClickable(cityButton);
                cityButton.click();
                return;
            }
        }
        LOGGER.warn("No matching city button found for: {}", cityName);
    }
}
