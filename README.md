# RFC UI Automation Framework

Standalone Selenium + TestNG framework for RFC (Release/Fix/Change Request) UI
testing. It is a **new project**, independent of the VRO UI automation
framework - no shared code, no shared dependency.

## Core lifecycle

```
Initialize Browser (once)
    -> Login (once)
    -> Authenticated Session
    -> RFC001 -> RFC002 -> RFC003 -> ... (sequential, same browser)
    -> Cleanup (browser closed once)
```

- Browser starts exactly once per suite run (`DriverManager.initializeDriver()`
  in `BaseTest.suiteSetup()`, a TestNG `@BeforeSuite`).
- Login happens exactly once (`LoginPage.login()`, also in `@BeforeSuite`).
- Every RFC test class extends `BaseTest` and reuses the same `WebDriver` and
  the same authenticated session - no test creates a driver, logs in, or
  quits the browser.
- The browser closes exactly once, in `@AfterSuite`.

This only holds as long as all RFC test classes are declared inside **one**
`<test>`/`<suite>` in `testng.xml` - TestNG's `@BeforeSuite`/`@AfterSuite`
scope to the XML suite, not to a class. See the comment in `testng.xml`.

## Session recovery

Before each `@Test` method, `BaseTest.recoverToDashboard()` runs:

1. Check whether the "Portfolio" nav link (the authenticated-shell marker) is
   present within 5 seconds (`DashboardPage.isLoaded()`), without throwing.
2. If it's **not** present, the session is treated as expired: re-login once,
   then wait for the dashboard to reload. This is the only path that ever
   logs in again after suite setup.
3. If the session **is** valid but the browser is on some other page (a
   previous RFC test navigated deeper into the app), navigate back to
   `base.url` via `driver.get()` rather than blindly pressing browser back.

This keeps `@BeforeMethod` cheap and non-destructive on the common path
(session still valid -> no navigation at all if already on the base URL).

## Project structure

```
rfc-ui-automation/
├── pom.xml
├── testng.xml
├── config.properties.example
├── src/main/java/com/vro/rfc/
│   ├── config/ConfigReader.java
│   ├── driver/DriverManager.java
│   ├── pages/{BasePage,LoginPage,DashboardPage}.java
│   ├── utils/{WaitUtils,ScreenshotUtils,TestDataUtils}.java
│   └── constants/FrameworkConstants.java
└── src/test/
    ├── java/com/vro/rfc/
    │   ├── base/BaseTest.java
    │   ├── tests/rfc/RFC001_Test.java
    │   └── listeners/TestListener.java
    └── resources/
        ├── config.properties
        ├── logback.xml
        └── testdata/
```

## Configuration

`src/test/resources/config.properties`:

```properties
base.url=https://advisorqa.valueresearch.in/signup
browser=chrome
headless=false
username=${ENV_USERNAME}
password=${ENV_PASSWORD}
explicit.wait=15
page.load.timeout=30
```

Credentials are **never** committed - `username`/`password` are placeholders
resolved from environment variables at runtime:

```bash
export ENV_USERNAME=your_qa_user@example.com
export ENV_PASSWORD='your_qa_password'
```

`browser` and `headless` can be overridden per run with `-D`, which takes
precedence over the properties file:

```bash
mvn clean test -Dbrowser=chrome -Dheadless=true
```

## Running tests

```bash
# Full RFC suite (testng.xml) - shared login/session across all RFC tests
mvn clean test

# Headless, CI-friendly
mvn clean test -Dheadless=true

# A single RFC class - best effort; -Dtest works when it's the only class
# TestNG resolves out of testng.xml. For a guaranteed isolated run of one RFC
# (its own login/browser), point surefire at a dedicated single-class suite
# XML instead, e.g.:
#   mvn clean test -Dsurefire.suiteXmlFiles=rfc001-only.xml
mvn clean test -Dtest=RFC001_Test
```

Reports land in `reports/RFC_Execution_Report_<timestamp>.html` (ExtentReports),
screenshots-on-failure in `screenshots/`, logs in `logs/rfc-automation.log`.

## Adding a new RFC

No changes to authentication/lifecycle code are ever required:

1. Add a Page Object under `src/main/java/com/vro/rfc/pages/` if the RFC
   touches a page that doesn't have one yet.
2. Add `src/test/java/com/vro/rfc/tests/rfc/RFC00N_Test.java` extending
   `BaseTest`. Use the inherited `dashboardPage` field (already
   authenticated, already recovered to the dashboard) plus your new page
   objects. Write only business logic and assertions.
3. Add test data under `src/test/resources/testdata/` if the RFC needs it
   (`TestDataUtils` supports `.properties`, `.json`, `.csv`).
4. Register the class in `testng.xml`'s `<classes>` block.

## What this framework deliberately does not do

- No parallel execution (`parallel="false"` in testng.xml) - all RFC tests
  intentionally share one authenticated browser session. Parallelizing would
  require separate sessions per thread, which is out of scope here.
- No `Thread.sleep()` - all waits go through `WaitUtils` (`WebDriverWait`).
- No framework-level abstractions beyond what's listed above (no service
  layers, no driver factories-of-factories, no DI container).
