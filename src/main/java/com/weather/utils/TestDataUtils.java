package com.weather.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Utility class for loading test data and configuration properties.
 */
public class TestDataUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataUtils.class);

    private static String environment;
    private static String browser;
    private static String baseUrl;
    private static boolean isHeadless;
    private static String username;
    private static String password;
    private static List<String> cities;

    /**
     * Loads the configuration properties.
     *
     * @param configFilePath the path to the configuration properties file
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static void loadConfigProperties(String configFilePath) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            LOGGER.info("Loading configuration properties from {}", configFilePath);
            properties.load(fis);
            environment = properties.getProperty("environment", "prod");
            browser = properties.getProperty("browser", "chrome");
            baseUrl = properties.getProperty("base_url", "https://weather.com");
            isHeadless = Boolean.parseBoolean(properties.getProperty("is_headless", "true"));
        } catch (IOException ex) {
            LOGGER.error("Failed to load configuration properties from {}", configFilePath, ex);
            throw ex;
        }
    }

    /**
     * Loads the test data.
     *
     * @param testDataFilePath the path to the test data file
     * @throws IOException if an I/O error occurs while reading the file
     */
    @SuppressWarnings("unchecked")
    public static void loadTestData(String testDataFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(testDataFilePath)) {
            LOGGER.info("Loading test data from {}", testDataFilePath);
            Yaml yaml = new Yaml();
            Map<String, Object> testData = yaml.load(fis);
            Map<String, Object> environmentData = (Map<String, Object>) testData.get(environment);
            username = (String) environmentData.get("username");
            password = (String) environmentData.get("password");
            Object citiesObj = testData.get("cities");
            if (citiesObj instanceof List) {
                cities = (List<String>) citiesObj;
            } else {
                cities = new ArrayList<>();
            }
        } catch (IOException ex) {
            LOGGER.error("Failed to load test data from {}", testDataFilePath, ex);
            throw ex;
        }
    }

    /**
     * Returns the browser to use.
     *
     * @return the browser name
     */
    public static String getBrowser() {
        return browser;
    }

    /**
     * Returns the base URL.
     *
     * @return the base URL
     */
    public static String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Returns the password.
     *
     * @return the password
     */
    public static String getPassword() {
        return password;
    }

    /**
     * Returns whether the test is running in headless mode.
     *
     * @return true if the test is running in headless mode, false otherwise
     */
    public static boolean isHeadless() {
        return isHeadless;
    }

    /**
     * Returns the list of test cities.
     *
     * @return list of city names
     */
    public static List<String> getCities() {
        return cities;
    }
}
