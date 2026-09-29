# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Standalone Selenium + TestNG UI automation framework for RFC (Release/Fix/Change
Request) testing against valueresearch.in QA environments. It is independent of
the VRO UI automation framework - no shared code, no shared dependency.

## Commands

```bash
# Full RFC suite (testng.xml) - shared login/session across all RFC tests
mvn clean test

# Headless, CI-friendly
mvn clean test -Dheadless=true

# Override browser (chrome/firefox/edge)
mvn clean test -Dbrowser=firefox

# A single RFC class - best effort; -Dtest works when it's the only class
# TestNG resolves out of testng.xml. For a guaranteed isolated run of one RFC
# (its own login/browser), point surefire at a dedicated single-class suite
# XML instead, e.g.:
#   mvn clean test -Dsurefire.suiteXmlFiles=rfc001-only.xml
mvn clean test -Dtest=RFC001_Test
```

Credentials come from environment variables, never the properties file:

```bash
export ENV_USERNAME=your_qa_user@example.com
export ENV_PASSWORD='your_qa_password'
```

Reports land in `reports/RFC_Execution_Report_<timestamp>.html` (ExtentReports),
screenshots-on-failure in `screenshots/`, logs in `logs/rfc-automation.log`.

## Core lifecycle - the architectural constraint everything else follows

```
Initialize Browser (once)
    -> Login (once)
    -> Authenticated Session
    -> RFC001 -> PortfolioPlannerTest -> ... (sequential, same browser)
    -> Cleanup (browser closed once)
```

- Browser starts exactly once per suite run (`DriverManager.initializeDriver()`
  in `BaseTest.suiteSetup()`, a TestNG `@BeforeSuite`).
- Login happens exactly once (`LoginPage.login()`, also in `@BeforeSuite`).
- Every RFC test class extends `BaseTest` and reuses the same static `WebDriver`
  and the same authenticated session - no test creates a driver, logs in, or
  quits the browser.
- The browser closes exactly once, in `@AfterSuite`.

This only holds as long as all RFC test classes are declared inside **one**
`<test>`/`<suite>` in `testng.xml` - TestNG's `@BeforeSuite`/`@AfterSuite` scope
to the XML suite, not to a class (see the comment at the top of `testng.xml`).
`parallel="false"` is intentional and must stay that way: all RFC tests share
one authenticated browser session, and parallelizing would require separate
sessions per thread, which is out of scope for this framework.

### Session recovery

Before each `@Test` method, `BaseTest.recoverToDashboard()` runs:

1. Check whether the "Portfolio" nav link (the authenticated-shell marker) is
   present within 5 seconds (`DashboardPage.isLoaded()`), without throwing.
2. If it's **not** present, the session is treated as expired: re-login once,
   then wait for the dashboard to reload. This is the only path that ever logs
   in again after suite setup.
3. If the session **is** valid but the browser is on some other page (a
   previous RFC test navigated deeper into the app), navigate back to
   `base.url` via `driver.get()` rather than blindly pressing browser back.

This keeps `@BeforeMethod` cheap and non-destructive on the common path
(session still valid -> no navigation at all if already on the base URL).

## Adding a new RFC

No changes to authentication/lifecycle code are ever required:

1. Add a Page Object under `src/main/java/com/vro/rfc/pages/` (extends
   `BasePage`, uses `@FindBy` + the injected `wait` (`WaitUtils`)) if the RFC
   touches a page that doesn't have one yet.
2. Add `src/test/java/com/vro/rfc/tests/rfc/RFC00N_Test.java` (or a
   feature-named class like `PortfolioPlannerTest`) extending `BaseTest`. Use
   the inherited `dashboardPage` field (already authenticated, already
   recovered to the dashboard) plus your new page objects. Write only business
   logic and assertions; call `logStep(...)` for every meaningful step so it
   shows up in both the console log and the ExtentReports HTML trace.
3. Add test data under `src/test/resources/testdata/` if the RFC needs it
   (`TestDataUtils` supports `.properties`, `.json`, `.csv`).
4. Register the class in `testng.xml`'s `<classes>` block, inside the existing
   `<test>` (not a new one - that would break the login-once guarantee).

## Locator strategy

Pages under test often have no `id`/`data-testid`/`name` attributes to key off
- classes are Tailwind utilities (sometimes arbitrary values like
`border-[2px]`) expected to churn with restyling. The convention (see
`PortfolioPlannerPage`) is: anchor locators on stable, human-facing text
(`normalize-space()` xpath matches on headings/button labels) first, and only
fall back to attributes like `data-slot="button"` when text alone isn't
unique enough. Don't introduce brittle class-based CSS selectors.

## Writing tests for a PRD

`PortfolioPlannerTest` is the reference example for PRD-driven suites: it mixes
real, executable `@Test` methods backed by verified locators with disabled
`@Test(enabled = false)` placeholders for PRD acceptance criteria that don't
have real locators/flows verified yet. This is deliberate documentation of
coverage intent - don't delete placeholders or fabricate locators just to make
them pass. Anything that would submit a real transaction (OTP authorization,
payment, past "Invest Now" landing on the Cart screen) is explicitly out of
scope for real execution and stays disabled.

## Rules enforced by BaseTest - do not violate in RFC test classes

- No test class creates a `WebDriver` (always go through `DriverManager`).
- No test class calls `login()` directly.
- No test class calls `driver.quit()`.
- No `Thread.sleep()` anywhere - all waits go through `WaitUtils`
  (`WebDriverWait`-backed: `waitForVisible`, `waitForClickable`,
  `waitForInvisible`, `waitForText`, `waitForUrlContains`,
  `isPresentWithin` for non-throwing checks).
- No framework-level abstractions beyond what's already there (no service
  layers, no driver factories-of-factories, no DI container).

## Configuration

`src/test/resources/config.properties`, resolution order is `-D` system
property > properties file > hardcoded default (`FrameworkConstants`). Values
shaped like `${ENV_VAR}` are resolved from the environment at runtime
(`ConfigReader.resolveEnv`) - this is how credentials stay out of the
properties file and out of git. `config.properties.example` is the committed
template; never put real credentials in either file.
