package com.weather.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base page class providing common WebDriver wait utilities for all page objects.
 */
public class BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);
    private static final int DEFAULT_WAIT_SECONDS = 20;

    protected WebDriverWait wait;
    protected WebDriver driver;

    /**
     * Constructs a BasePage with the given WebDriver.
     *
     * @param driver the WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    /**
     * Waits for an element to be visible.
     *
     * @param element the WebElement to wait for
     */
    protected void waitForElementToBeVisible(WebElement element) {
        LOGGER.debug("Waiting for element to be visible: {}", element);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param element the WebElement to wait for
     */
    protected void waitForElementToBeClickable(WebElement element) {
        LOGGER.debug("Waiting for element to be clickable: {}", element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits for an element to contain the specified text.
     *
     * @param element the WebElement to wait for
     * @param text    the expected text
     */
    protected void waitForElementToContainText(WebElement element, String text) {
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    /**
     * Waits for an element attribute to have a specific value.
     *
     * @param element   the WebElement
     * @param attribute the attribute name
     * @param value     the expected value
     */
    protected void waitForElementAttributeToBe(WebElement element, String attribute, String value) {
        wait.until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    /**
     * Waits for an element to disappear.
     *
     * @param element the WebElement to wait for
     */
    protected void waitForElementToDisappear(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Waits for page title to contain specific text.
     *
     * @param title the expected title text
     */
    protected void waitForPageTitleToContain(String title) {
        wait.until(ExpectedConditions.titleContains(title));
    }

    /**
     * Returns the page title.
     *
     * @return the page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Returns true if the element is displayed (waits for visibility first).
     *
     * @param element the WebElement
     * @return true if displayed
     */
    protected boolean isElementDisplayed(WebElement element) {
        waitForElementToBeVisible(element);
        return element.isDisplayed();
    }
}
