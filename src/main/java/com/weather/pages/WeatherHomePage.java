package com.weather.pages;

import com.weather.automation.helpers.ElementActions;
import com.weather.automation.utils.DriverUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This is a page class which contains different elements from the home page and also the methods related to them
 *
 * @author nidhinraj.pp
 */

public class WeatherHomePage {

    private final WebDriverWait wait = DriverUtils.getWait();
    ElementActions elementActions = new ElementActions(wait);
    private WebDriver driver;

    @FindBy(css = "#LocationSearch_input")
    private WebElement searchBox;

    @FindBy(css = "")
    private WebElement searchButton;

    public WeatherHomePage(WebDriver driver) {
    }
}
