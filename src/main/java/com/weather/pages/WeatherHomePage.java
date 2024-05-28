package com.weather.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * This is a page class which contains different elements from the home page and also the methods related to them
 */
public class WeatherHomePage {

    private final WebDriver driver;

    @FindBy(id = "LocationSearch_input")
    private WebElement locationSearchInput;

    @FindBy(css = "a > span.styles--locationName--1R6PN")
    private WebElement locationName;

    @FindBy(css = "div > span[class*='LanguageSelector--unitDisplay']")
    private WebElement languageSelectorUnitDisplay;

    /**
     * Constructor for WeatherHomePage class
     *
     * @param driver the WebDriver instance
     */
    public WeatherHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void inputLocation(String location) {
        locationSearchInput.click();
        locationSearchInput.sendKeys(location);
        selectCity(location);
    }

    // Method to select the first city from the list that matches the provided string parameter
    public void selectCity(String cityName) {
        WebElement listContainer = driver.findElement(By.id("LocationSearch_listbox"));
        List<WebElement> cityButtons = listContainer.findElements(By.tagName("button"));
        for (WebElement cityButton : cityButtons) {
            String buttonText = cityButton.getText();
            if (buttonText.contains(cityName)) {
                cityButton.click();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.invisibilityOf(cityButton));
                break;
            }
        }
    }
}
