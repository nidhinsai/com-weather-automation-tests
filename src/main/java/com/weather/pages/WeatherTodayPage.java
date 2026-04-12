package com.weather.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page object for the Weather.com Today page.
 * Contains elements and methods for verifying weather conditions.
 */
public class WeatherTodayPage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherTodayPage.class);

    @FindBy(css = "[data-testid=CurrentConditionsContainer]")
    private WebElement currentConditionsContainer;

    @FindBy(css = "[data-testid=TemperatureValue]")
    private WebElement temperatureValue;

    @FindBy(css = "[data-testid=wxPhrase]")
    private WebElement weatherPhrase;

    /**
     * Constructor for WeatherTodayPage.
     *
     * @param driver the WebDriver instance
     */
    public WeatherTodayPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Returns true if the current conditions container is displayed.
     *
     * @return true if displayed
     */
    public boolean isCurrentConditionsDisplayed() {
        LOGGER.info("Checking if current conditions container is displayed");
        return isElementDisplayed(currentConditionsContainer);
    }

    /**
     * Returns true if the temperature value element is displayed.
     *
     * @return true if displayed
     */
    public boolean isTemperatureValueDisplayed() {
        LOGGER.info("Checking if temperature value is displayed");
        return isElementDisplayed(temperatureValue);
    }

    /**
     * Returns true if the weather phrase element is displayed.
     *
     * @return true if displayed
     */
    public boolean isWeatherPhraseDisplayed() {
        LOGGER.info("Checking if weather phrase is displayed");
        return isElementDisplayed(weatherPhrase);
    }

    /**
     * Returns the displayed temperature value text.
     *
     * @return temperature as string
     */
    public String getTemperatureText() {
        waitForElementToBeVisible(temperatureValue);
        return temperatureValue.getText();
    }
}
