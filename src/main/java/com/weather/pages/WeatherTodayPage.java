package com.weather.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class WeatherTodayPage extends BasePage {

    @FindBy(xpath = "//div[@class='CurrentConditions--columns--30npQ']")
    private WebElement currentConditionsContainer;

    @FindBy(xpath = "//span[@data-testid='TemperatureValue']")
    private WebElement temperatureValue;

    public WeatherTodayPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean isCurrentConditionsDisplayed() {
        waitForElementToBeVisible(currentConditionsContainer);
        return currentConditionsContainer.isDisplayed();
    }

    public boolean isTemperatureValueDisplayed() {
        return isElementDisplayed(temperatureValue);
    }

}
