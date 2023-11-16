package com.weather.pages;

import com.weather.helpers.ElementActions;
import com.weather.utils.DriverUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This is a page class which contains different elements from the home page and also the methods related to them
 */
public class WeatherHomePage {

    private final WebDriverWait wait;
    private final ElementActions elementActions;
    private final WebDriver driver;

    @FindBy(css = "#LocationSearch_input")
    private WebElement searchBox;

    @FindBy(css = "")
    private WebElement searchButton;

    /**
     * Constructor for WeatherHomePage class
     *
     * @param driver the WebDriver instance
     */
    public WeatherHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = DriverUtils.getWait();
        this.elementActions = new ElementActions(wait);
    }
}
