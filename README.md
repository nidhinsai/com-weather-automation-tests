# com-weather-automation-tests

End-to-end Selenium/TestNG automation suite for **[OpenWeatherMap](https://openweathermap.org)** built as a portfolio-quality framework.

[![CI Pipeline](https://github.com/nidhinsai/com-weather-automation-tests/actions/workflows/ci.yml/badge.svg)](https://github.com/nidhinsai/com-weather-automation-tests/actions/workflows/ci.yml)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Test framework | TestNG 7.10 |
| Browser automation | Selenium 4.23 |
| Driver management | WebDriverManager 5.9 |
| Build | Maven 3.9 |
| Reporting | Surefire XML + Artifacts |
| Static analysis | Checkstyle + PMD |
| Logging | SLF4J + Logback |
| CI | GitHub Actions (Ubuntu, Chrome headless) |

---

## Project Structure

```
src/
├── main/java/com/weather/
│   ├── pages/
│   │   ├── BasePage.java            # Common wait/assertion helpers
│   │   ├── WeatherHomePage.java     # Search flow (navigate to /find?q=)
│   │   └── WeatherTodayPage.java    # City weather page assertions
│   └── utils/
│       ├── TestDataUtils.java       # YAML/properties loader
│       └── WebDriverProvider.java   # Browser factory with stealth options
└── test/
    ├── java/com/weather/automation/
    │   ├── listeners/TestListener.java   # Screenshot on failure
    │   └── tests/
    │       ├── BaseTest.java             # Driver lifecycle + page-object init
    │       └── WeatherSearchTests.java   # Data-driven city search tests
    └── resources/
        ├── config.properties
        ├── testdata/test_data.yml        # City list (Amsterdam, London, Tokyo)
        └── testng.xml
```

---

## Running Locally

```bash
# Run all tests (headless Chrome)
mvn test

# Run with a specific browser
mvn test -Dbrowser=firefox

# Run with visible browser window
mvn test -Dis_headless=false

# Static analysis only
mvn checkstyle:check pmd:check
```

---

## Test Scenarios

| Test | Description |
|---|---|
| `test_searchCityAndCheckTemperatureToday` | Searches each city and verifies current conditions + temperature |
| `test_pageTitleIsCorrect` | Asserts OWM page title contains weather-related text |
| `test_cityPageUrlContainsCitySegment` | Verifies successful city-page navigation via URL |

Cities are data-driven from `testdata/test_data.yml` — extend by adding entries under `cities:`.

---

## CI/CD

The GitHub Actions pipeline runs on every push/PR to `master`:
1. Compile (no tests)
2. Checkstyle validation
3. PMD static analysis
4. Full TestNG test run (Chrome headless on Ubuntu)
5. Upload Surefire reports & failure screenshots as artifacts
