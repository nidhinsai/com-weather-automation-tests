package com.weather.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Utility class for loading test data and configuration properties.
 */
public class TestDataUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataUtils.class);

    private static String environment;
    private static String driverLocation;
    private static boolean isHeadless;
    private static String username;
    private static String password;

    /**
     * Loads the configuration properties.
     *
     * @param configFilePath the path to the configuration properties file
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static void loadConfigProperties(String configFilePath) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            LOGGER.info("Loading configuration properties");
            properties.load(fis);
            environment = properties.getProperty("environment");
            driverLocation = properties.getProperty("driver_location");
            isHeadless = Boolean.parseBoolean(properties.getProperty("is_headless"));
        } catch (IOException ex) {
            LOGGER.error("Failed to load configuration properties", ex);
            throw ex;
        }
    }

    /**
     * Loads the test data.
     *
     * @param testDataFilePath the path to the test data file
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static void loadTestData(String testDataFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(testDataFilePath)) {
            LOGGER.info("Loading test data");
            Yaml yaml = new Yaml();
            Map<String, Map<String, String>> testData = yaml.load(fis);
            Map<String, String> environmentData = testData.get(environment);
            username = environmentData.get("username");
            password = environmentData.get("password");
        } catch (IOException ex) {
            LOGGER.error("Failed to load test data", ex);
            throw ex;
        }
    }

    /**
     * Returns the driver location.
     *
     * @return the driver location
     */
    public static String getDriverLocation() {
        return driverLocation;
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
}
