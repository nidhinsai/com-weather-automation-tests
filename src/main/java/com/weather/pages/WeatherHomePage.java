package com.weather.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
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
     * Dismisses any cookie consent or privacy overlay that may block interactions.
     */
    private void dismissCookieConsent() {
        String[] consentSelectors = {
            "button[id*='accept']",
            "button[class*='accept']",
            "button[data-testid*='accept']",
            "#onetrust-accept-btn-handler",
            ".onetrust-accept-btn-handler",
            "[aria-label*='Accept']",
            "[aria-label*='accept']",
            "button[class*='consent']",
            "button[class*='agree']"
        };
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        for (String selector : consentSelectors) {
            try {
                WebElement btn = shortWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
                LOGGER.info("Dismissing cookie/consent dialog with selector: {}", selector);
                btn.click();
                return;
            } catch (TimeoutException | NoSuchElementException e) {
                // Try next selector
            }
        }
        LOGGER.debug("No cookie consent dialog found - proceeding");
    }

    /**
     * Scrolls the given element into view using JavaScript and clicks it.
     *
     * @param element the element to scroll to and click
     */
    private void jsClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Inputs a location in the search box and selects the matching city from the dropdown.
     *
     * @param location the city name to search for
     * @return a WeatherTodayPage after selecting the city
     */
    public WeatherTodayPage inputLocation(String location) {
        LOGGER.info("Searching for location: {}", location);
        dismissCookieConsent();
        try {
            waitForElementToBeClickable(locationSearchInput);
            locationSearchInput.click();
        } catch (TimeoutException e) {
            LOGGER.warn("Regular click timed out, falling back to JS click for search input");
            jsClick(locationSearchInput);
        }
        locationSearchInput.clear();
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
                try {
                    waitForElementToBeClickable(cityButton);
                    cityButton.click();
                } catch (TimeoutException e) {
                    LOGGER.warn("Regular city button click timed out, using JS click");
                    jsClick(cityButton);
                }
                return;
            }
        }
        LOGGER.warn("No matching city button found for: {}", cityName);
    }
}
