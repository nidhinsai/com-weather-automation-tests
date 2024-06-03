package com.weather.automation.tests;

import com.weather.automation.listeners.TestListener;
import com.weather.pages.WeatherHomePage;
import com.weather.pages.WeatherTodayPage;
import com.weather.utils.TestDataUtils;
import com.weather.utils.WebDriverProvider;
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
 * This class represents the test class for weather automation tests.
 */
@Listeners(TestListener.class)
public class BaseTest {

    private static final String TEST_RESOURCES_ROOT = "src/test/resources/";
    private static final String CONFIG_FILE_PATH = TEST_RESOURCES_ROOT + "config.properties";
    private static final String TEST_DATA_FILE_PATH = TEST_RESOURCES_ROOT + "testdata/test_data.yml";
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private static WebDriver driver;
    private static ITestContext testContext;
    private static WebDriverProvider webDriverProvider;
    protected WeatherHomePage weatherHomePage;
    protected WeatherTodayPage weatherTodayPage;

    /**
     * Returns the test context.
     *
     * @return the test context
     */
    public static ITestContext getTestContext() {
        return testContext;
    }

    /**
     * Sets the test context.
     *
     * @param context the test context
     */
    public static void setTestContext(ITestContext context) {
        testContext = context;
    }

    /**
     * Setup method to be run before the test class.
     * Loads the config properties and test data.
     * Initializes the WebDriver and sets it as an attribute in the test context.
     *
     * @param context the test context
     */
    @BeforeClass(alwaysRun = true)
    protected void classSetup(ITestContext context) {
        setTestContext(context);
        try {
            TestDataUtils.loadConfigProperties(CONFIG_FILE_PATH);
            TestDataUtils.loadTestData(TEST_DATA_FILE_PATH);
        } catch (IOException e) {
            logger.error("Class setup failed.", e);
        }
        webDriverProvider = new WebDriverProvider("Safari");
        driver = webDriverProvider.getDriver();
        getTestContext().setAttribute("WebDriver", driver);
    }

    /**
     * Method to be run before each test method.
     * Initializes the WeatherHomePage object.
     */
    @BeforeMethod(alwaysRun = true)
    protected void setupMethod() {
        weatherHomePage = new WeatherHomePage(driver);
    }

    /**
     * Method to be run after the test class.
     * Quits the WebDriver.
     */
    @AfterClass(alwaysRun = true)
    protected void tearDown() {
        webDriverProvider.quitDriver();
    }

    /**
     * Opens the specified URL in the browser.
     *
     * @param url the URL to open
     */
    protected void openUrl(String url) {
        logger.info("Opening URL: " + url);
        driver.get(url);
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }
}
