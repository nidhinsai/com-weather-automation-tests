package com.weather.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class TestDataUtils {
    protected static final Logger logger = LoggerFactory.getLogger(TestDataUtils.class);
    private static final String TEST_RESOURCES_ROOT = "src/test/resources/";
    private static final String CONFIG_FILE_PATH = TEST_RESOURCES_ROOT + "config.properties";
    private static final String TEST_DATA_FILE_PATH = TEST_RESOURCES_ROOT + "testdata/test_data.yml";
    private static String environment;
    private static String driverLocation;
    private static boolean isHeadless;
    private static String username;
    private static String password;

    public static void loadConfigProperties() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            logger.info("Loading configuration properties");
            properties.load(fis);
            environment = properties.getProperty("environment");
            driverLocation = properties.getProperty("driver_location");
            isHeadless = Boolean.parseBoolean(properties.getProperty("is_headless"));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Failed to load configuration properties");
        }
    }

    public static void loadTestData() throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(TEST_DATA_FILE_PATH)) {
            logger.info("Loading test data");
            Yaml yaml = new Yaml();
            Map<String, Map<String, String>> credentials = yaml.load(fileInputStream);
            Map<String, String> environmentData = credentials.get(environment);
            username = environmentData.get("username");
            password = environmentData.get("password");
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Failed to load test data");
        }
    }

    public static String getDriverLocation() {
        return driverLocation;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static Boolean isHeadless() {
        return isHeadless;
    }

}
