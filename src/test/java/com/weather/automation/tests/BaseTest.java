package com.weather.automation.tests;

import com.weather.automation.listeners.TestListener;
import com.weather.automation.utils.DriverUtils;
import com.weather.automation.utils.TestDataUtils;
import com.weather.pages.WeatherHomePage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.io.IOException;


@Listeners(TestListener.class)
public class BaseTest extends DriverUtils {

    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private static WebDriver driver;
    private static ITestContext testContext;
    protected WeatherHomePage weatherHomePage;

    public static ITestContext getTestContext() {
        return testContext;
    }

    public static void setTestContext(ITestContext context) {
        testContext = context;
    }

    @BeforeClass(alwaysRun = true)
    protected void classSetup(ITestContext context) {
        setTestContext(context);
        try {
            TestDataUtils.loadConfigProperties();
            TestDataUtils.loadTestData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = DriverUtils.getDriver();
        getTestContext().setAttribute("WebDriver", driver);
    }

    @BeforeMethod(alwaysRun = true)
    protected void setupMethod() {
        weatherHomePage = new WeatherHomePage(driver);
    }

    @AfterClass(alwaysRun = true)
    protected void tearDown() {
        DriverUtils.quitDriver();
    }

    protected void openUrl(String url) {
        logger.info("Opening URL {}", url);
        getDriver().get(url);
    }
}

