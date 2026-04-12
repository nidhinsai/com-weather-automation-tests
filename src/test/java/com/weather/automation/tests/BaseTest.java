package com.weather.automation.tests;

import com.weather.automation.listeners.TestListener;
import com.weather.pages.WeatherHomePage;
import com.weather.pages.WeatherTodayPage;
import com.weather.utils.TestDataUtils;
import com.weather.utils.WebDriverProvider;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.io.IOException;

/**
 * Base test class that handles WebDriver lifecycle, config loading, and common utilities.
 */
@Listeners(TestListener.class)
public class BaseTest {

    private static final String TEST_RESOURCES_ROOT = "src/test/resources/";
    private static final String CONFIG_FILE_PATH = TEST_RESOURCES_ROOT + "config.properties";
    private static final String TEST_DATA_FILE_PATH = TEST_RESOURCES_ROOT + "testdata/test_data.yml";

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

    private static WebDriver driver;
    private static ITestContext testContext;
    private static WebDriverProvider webDriverProvider;

    protected WeatherHomePage weatherHomePage;
    protected WeatherTodayPage weatherTodayPage;

    /**
     * Returns the shared test context.
     *
     * @return the ITestContext
     */
    public static ITestContext getTestContext() {
        return testContext;
    }

    /**
     * Sets the shared test context.
     *
     * @param context the ITestContext to set
     */
    public static void setTestContext(ITestContext context) {
        testContext = context;
    }

    /**
     * Runs before the test class. Loads config/test data and initialises the WebDriver.
     *
     * @param context the TestNG test context
     */
    @BeforeClass(alwaysRun = true)
    protected void classSetup(ITestContext context) {
        setTestContext(context);
        try {
            TestDataUtils.loadConfigProperties(CONFIG_FILE_PATH);
            TestDataUtils.loadTestData(TEST_DATA_FILE_PATH);
        } catch (IOException e) {
            LOGGER.error("Class setup failed while loading config/test data", e);
            throw new RuntimeException("Failed to load test configuration", e);
        }
        String browser = TestDataUtils.getBrowser();
        boolean headless = TestDataUtils.isHeadless();
        webDriverProvider = new WebDriverProvider(browser, headless);
        driver = webDriverProvider.getDriver();
        getTestContext().setAttribute("WebDriver", driver);
        LOGGER.info("WebDriver initialised: {} (headless={})", browser, headless);
    }

    /**
     * Runs before each test method. Navigates to the base URL, applies stealth JS,
     * and sets up page objects.
     */
    @BeforeMethod(alwaysRun = true)
    protected void setupMethod() {
        String baseUrl = TestDataUtils.getBaseUrl();
        LOGGER.info("Navigating to base URL: {}", baseUrl);
        driver.get(baseUrl);
        applyStealthJs();
        weatherHomePage = new WeatherHomePage(driver);
    }

    /**
     * Injects JavaScript to reduce bot-detection signals after page navigation.
     * Removes the navigator.webdriver property that headless Chrome exposes.
     */
    private void applyStealthJs() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                "Object.defineProperty(navigator, \'webdriver\', {get: () => undefined});"
            );
            LOGGER.debug("Stealth JS applied successfully");
        } catch (Exception e) {
            LOGGER.warn("Could not apply stealth JS: {}", e.getMessage());
        }
    }

    /**
     * Runs after the test class. Quits the WebDriver.
     */
    @AfterClass(alwaysRun = true)
    protected void tearDown() {
        if (webDriverProvider != null) {
            webDriverProvider.quitDriver();
        }
    }

    /**
     * Navigates the browser to the given URL.
     *
     * @param url the URL to open
     */
    protected void openUrl(String url) {
        LOGGER.info("Opening URL: {}", url);
        driver.get(url);
    }

    /**
     * Returns the current page title.
     *
     * @return the page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
}
