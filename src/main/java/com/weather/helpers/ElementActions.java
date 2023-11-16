package com.weather.helpers;

import com.weather.utils.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class provides methods to perform actions on web elements.
 */
public class ElementActions {

    private final WebDriverWait wait;

    /**
     * Constructor to initialize the WebDriverWait object.
     *
     * @param wait the WebDriverWait object
     */
    public ElementActions(WebDriverWait wait) {
        this.wait = wait;
    }

    /**
     * Clicks on the element identified by the given locator.
     *
     * @param locator the locator to identify the element
     */
    public void clickElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Clicks on the given element.
     *
     * @param element the element to be clicked
     */
    public void clickElement(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Checks if the given element is displayed.
     *
     * @param element the element to be checked
     * @return true if the element is displayed, false otherwise
     */
    public boolean isElementDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Scrolls to the given element and clicks on it.
     *
     * @param element the element to be scrolled to and clicked
     */
    public void scrollAndClick(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        ((JavascriptExecutor) DriverUtils.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    /**
     * Performs a double click on the given element.
     *
     * @param element the element to be double-clicked
     */
    public void doubleClick(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        Actions actions = new Actions(DriverUtils.getDriver());
        actions.doubleClick(element).perform();
    }

}

