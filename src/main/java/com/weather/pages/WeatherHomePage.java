package com.weather.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
            "#onetrust-accept-btn-handler",
            "button[id*='accept']",
            "button[data-testid*='accept']",
            "[aria-label*='Accept']"
        };
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        for (String selector : consentSelectors) {
            try {
                WebElement btn = shortWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
                LOGGER.info("Dismissing consent dialog with selector: {}", selector);
                btn.click();
                return;
            } catch (TimeoutException | NoSuchElementException e) {
                LOGGER.debug("Consent selector not matched: {} - {}", selector, e.getMessage());
            }
        }
        LOGGER.debug("No cookie consent dialog found - proceeding");
    }

    /**
     * Uses JavaScript to scroll the element into view and click it.
     *
     * @param element the element to click
     */
    private void jsClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Inputs a location in the search box and selects the matching city from the dropdown.
     * Uses Actions class to ensure proper focus and keyboard events are triggered.
     *
     * @param location the city name to search for
     * @return a WeatherTodayPage after selecting the city
     */
    public WeatherTodayPage inputLocation(String location) {
        LOGGER.info("Searching for location: {}", location);
        dismissCookieConsent();

        // Wait for search input to be present in DOM regardless of clickability
        WebDriverWait presenceWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        presenceWait.until(ExpectedConditions.presenceOfElementLocated(By.id("LocationSearch_input")));

        // Use Actions to properly focus and type (fires all native keyboard events)
        Actions actions = new Actions(driver);
        try {
            waitForElementToBeClickable(locationSearchInput);
            actions.moveToElement(locationSearchInput).click().sendKeys(location).perform();
        } catch (TimeoutException e) {
            LOGGER.warn("Regular click timed out, using JS click + Actions sendKeys: {}", e.getMessage());
            jsClick(locationSearchInput);
            actions.sendKeys(locationSearchInput, location).perform();
        }

        LOGGER.info("Waiting for suggestion listbox to appear");
        waitForElementToBeVisible(locationSearchListBox);
        selectCity(location);
        return new WeatherTodayPage(driver);
    }

    /**
     * Selects the first city from the suggestion dropdown that matches the given city name.
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
                    LOGGER.warn("City button click timed out, using JS click: {}", e.getMessage());
                    jsClick(cityButton);
                }
                return;
            }
        }
        // If no exact match, click the first button as a fallback
        LOGGER.warn("No matching city button for: {} - clicking first suggestion", cityName);
        cityButtons.get(0).click();
    }
}
