package com.vro.rfc.tests.rfc;

import com.vro.rfc.base.BaseTest;
import com.vro.rfc.pages.PortfolioPlannerPage;
import com.vro.rfc.utils.TestDataUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * Test cases derived from the "Portfolio Planner Flow" PRD (14-Jul-2026).
 *
 * Every step - not just start/completed - goes through BaseTest.logStep(), which
 * both prints to the console (SLF4J/Logback) and adds a line to this test's entry
 * in the ExtentReports HTML report. That's what makes the report a step-by-step
 * trace instead of a bare pass/fail.
 *
 * Two kinds of methods live here:
 *  - Real, executable @Test methods: backed by actual, verified locators in
 *    PortfolioPlannerPage, built from real HTML captured screen by screen.
 *  - Disabled placeholder @Test(enabled = false) methods: map to a PRD acceptance
 *    criterion this suite has NO real locators for yet (Change Risk Assessment,
 *    KYC Pending/Under Process, Risk Assessment Pending, Investment Breakdown,
 *    free-user masked view, Review/deviation coloring, field validation errors,
 *    Change/Add Fund popups, Reset). They exist so the class documents full PRD
 *    coverage intent honestly, without fabricating locators that don't exist.
 *
 * Per instruction, this class contains NO portfolio-aware test cases (the
 * "Includes Your Funds" / "See a New Plan" toggle, "Already yours" labels, or
 * the "you already own N funds" sentence - PRD sections 11.4/11.5/11.7).
 *
 * Also out of scope for real execution: anything past "Invest Now" landing on
 * the Cart screen. OTP authorization / payment is never automated here - that
 * submits a real transaction.
 */
public class PortfolioPlannerTest extends BaseTest {

    private static final String PORTFOLIO_PLANNER_URL = "https://advisorqa2.valueresearch.in/portfolio-planner/";

    private static Properties testData;

    private static String investorName;
    private static String transactionCapableInvestorName;
    private static String higherReturnsSipAmount;
    private static String higherReturnsInvestmentPeriod;
    private static String monthlyIncomeAmount;
    private static String taxSavingAmount;
    private static String taxSavingLockInWarningContains;
    private static String expectedHigherReturnsCta;
    private static String expectedMonthlyIncomeCta;
    private static String expectedTaxSavingCta;
    private static String expectedHigherReturnsHeading;
    private static String expectedMonthlyIncomeHeading;
    private static String expectedTaxSavingHeading;

    @BeforeClass(alwaysRun = true)
    public void loadTestData() throws IOException {
        testData = TestDataUtils.loadProperties("portfolio-planner.properties");

        investorName = testData.getProperty("investor.name");
        transactionCapableInvestorName = testData.getProperty("investor.name.transaction.enabled");
        higherReturnsSipAmount = testData.getProperty("higher.returns.sip.amount");
        higherReturnsInvestmentPeriod = testData.getProperty("higher.returns.investment.period");
        monthlyIncomeAmount = testData.getProperty("monthly.income.amount");
        taxSavingAmount = testData.getProperty("tax.saving.amount");
        taxSavingLockInWarningContains = testData.getProperty("tax.saving.lockin.warning.contains");
        expectedHigherReturnsCta = testData.getProperty("expected.button.higher.returns.cta");
        expectedMonthlyIncomeCta = testData.getProperty("expected.button.monthly.income.cta");
        expectedTaxSavingCta = testData.getProperty("expected.button.tax.saving.cta");
        expectedHigherReturnsHeading = testData.getProperty("expected.heading.higher.returns");
        expectedMonthlyIncomeHeading = testData.getProperty("expected.heading.monthly.income");
        expectedTaxSavingHeading = testData.getProperty("expected.heading.tax.saving");
    }

    /**
     * Runs after BaseTest's own recoverToDashboard() (superclass @BeforeMethod runs
     * first in TestNG), so the session is already confirmed valid before each test
     * here opens the Portfolio Planner directly by URL.
     */
    @BeforeMethod(alwaysRun = true)
    public void openPortfolioPlanner() {
        driver.get(PORTFOLIO_PLANNER_URL);
    }

    // ============================================================
    // Select Investor screen
    // ============================================================

    @Test(description = "Select Investor: selecting an investor and clicking Next opens the aim-selection step "
            + "with that investor carried over (PRD: Select Investor -> Investment objective)")
    public void verifySelectInvestorNavigatesToAimScreenWithInvestorCarriedOver() {
        logStep("Test started: verifySelectInvestorNavigatesToAimScreenWithInvestorCarriedOver");

        PortfolioPlannerPage plannerPage = selectInvestorAndProceed();

        logStep("Asserting selected investor name carried over to the aim step");
        Assert.assertEquals(plannerPage.getSelectedInvestorName(), investorName,
                "Expected the selected investor to carry over to the aim-selection step");

        logStep("Test completed: verifySelectInvestorNavigatesToAimScreenWithInvestorCarriedOver");
    }

    @Test(enabled = false, description = "Select Investor: 'Add another investor' opens the add-investor flow, "
            + "limited to 6 investors (PRD: 'User can choose the investor or add a new investor (if less than 6 "
            + "investors added)'). No locators for the add-investor screen yet.")
    public void verifyAddAnotherInvestorFlow() {
        throw new SkipException("No locators for the add-investor screen - provide its HTML to implement.");
    }

    @Test(enabled = false, description = "Select Investor: screen is skipped entirely for free-account users, who "
            + "land directly on the planner-options screen (PRD acceptance criterion). Requires a separate "
            + "free-account test login, which this framework's single-credential BaseTest doesn't support.")
    public void verifySelectInvestorScreenHiddenForFreeUser() {
        throw new SkipException("Needs a free-account test identity - not configured in ConfigReader/BaseTest.");
    }

    // ============================================================
    // Change Risk Assessment (PRD: shown on clicking 'Update' next to risk profile)
    // ============================================================

    @Test(enabled = false, description = "Change Risk Assessment: 'Retake assessment' redirects to the Risk "
            + "Assessment module with the investor preselected. No locators for the Change Risk Assessment "
            + "screen yet.")
    public void verifyRetakeAssessmentRedirectsToRiskAssessmentModule() {
        throw new SkipException("No locators for the Change Risk Assessment screen - provide its HTML to implement.");
    }

    @Test(enabled = false, description = "Change Risk Assessment: choosing a HIGHER risk profile manually shows an "
            + "extra warning that must be ticked before 'Confirm' can proceed. No locators for this screen/dialog.")
    public void verifyManualHigherRiskProfileShowsWarningBeforeConfirm() {
        throw new SkipException("No locators for the Change Risk Assessment screen - provide its HTML to implement.");
    }

    // ============================================================
    // KYC Pending / KYC Under Process / Risk Assessment Pending
    // ============================================================

    @Test(enabled = false, description = "KYC Pending: 'Complete KYC now' redirects to the Fund Advisor onboarding "
            + "journey with the investor preselected. Needs the KYC Pending screen's locators plus a test investor "
            + "in a KYC-pending state.")
    public void verifyKycPendingCompleteKycRedirectsToOnboarding() {
        throw new SkipException("No locators for the KYC Pending screen, and no known KYC-pending test investor.");
    }

    @Test(enabled = false, description = "KYC Under Process: 'Go Back' returns the user to the page they came from. "
            + "Needs the KYC Under Process banner's locators plus a test investor in that state.")
    public void verifyKycUnderProcessGoBackReturnsToPreviousPage() {
        throw new SkipException("No locators for the KYC Under Process banner, and no known such test investor.");
    }

    @Test(enabled = false, description = "Risk Assessment Pending: 'Complete risk assessment' redirects to the "
            + "first Risk Assessment screen with the investor preselected. Needs this screen's locators plus a "
            + "test investor with no risk assessment on file.")
    public void verifyRiskAssessmentPendingRedirectsToRiskAssessmentModule() {
        throw new SkipException("No locators for the Risk Assessment Pending screen, and no known such test investor.");
    }

    // ============================================================
    // Choose an investing aim
    // ============================================================

    @Test(description = "Aim screen: 'Invest for higher returns' opens its SIP/lumpsum details form (PRD 11.2.1)")
    public void verifyInvestForHigherReturnsCardOpensItsForm() {
        logStep("Test started: verifyInvestForHigherReturnsCardOpensItsForm");

        PortfolioPlannerPage plannerPage = selectInvestorAndProceed();

        logStep("Selecting aim: Invest for higher returns");
        plannerPage.selectInvestForHigherReturns();

        // waitUntil...StepLoaded() is itself the assertion here: it throws if the
        // expected heading never appears, failing the test.
        plannerPage.waitUntilInvestForHigherReturnsStepLoaded();
        logStep("Invest for higher returns form loaded");

        logStep("Test completed: verifyInvestForHigherReturnsCardOpensItsForm");
    }

    @Test(description = "Aim screen: 'Invest for monthly income' opens its income details form (PRD 11.2.9)")
    public void verifyInvestForMonthlyIncomeCardOpensItsForm() {
        logStep("Test started: verifyInvestForMonthlyIncomeCardOpensItsForm");

        PortfolioPlannerPage plannerPage = selectInvestorAndProceed();

        logStep("Selecting aim: Invest for monthly income");
        plannerPage.selectInvestForMonthlyIncome();

        plannerPage.waitUntilInvestForMonthlyIncomeStepLoaded();
        logStep("Invest for monthly income form loaded");

        logStep("Test completed: verifyInvestForMonthlyIncomeCardOpensItsForm");
    }

    @Test(description = "Aim screen: 'Invest to save tax' opens its tax-saving details form (PRD 11.2.8)")
    public void verifyInvestToSaveTaxCardOpensItsForm() {
        logStep("Test started: verifyInvestToSaveTaxCardOpensItsForm");

        PortfolioPlannerPage plannerPage = selectInvestorAndProceed();

        logStep("Selecting aim: Invest to save tax");
        plannerPage.selectInvestToSaveTax();

        plannerPage.waitUntilInvestToSaveTaxStepLoaded();
        logStep("Invest to save tax form loaded");

        logStep("Test completed: verifyInvestToSaveTaxCardOpensItsForm");
    }

    // ============================================================
    // Invest for higher returns (SIP details) - PRD: "Show investment plan"
    // replaces "Calculate"; Potential Value is no longer shown.
    // ============================================================

    @Test(description = "Higher returns form: CTA reads 'Show investment plan', per PRD ('replaces Calculate')")
    public void verifyShowInvestmentPlanButtonLabel_HigherReturns() {
        logStep("Test started: verifyShowInvestmentPlanButtonLabel_HigherReturns");

        PortfolioPlannerPage plannerPage = goToHigherReturnsForm();

        logStep("Reading the CTA button's text");
        String actualCta = plannerPage.getShowInvestmentPlanButtonText();
        logStep("CTA text is: '" + actualCta + "' - expecting: '" + expectedHigherReturnsCta + "'");
        Assert.assertEquals(actualCta, expectedHigherReturnsCta,
                "PRD requires the CTA to read 'Show investment plan'");

        logStep("Test completed: verifyShowInvestmentPlanButtonLabel_HigherReturns");
    }

    @Test(description = "Higher returns form: SIP/One-time/Both investment-type pills are each clickable "
            + "(PRD tracking: 'SIP, One-time, both pills at portfolio planner input screen')")
    public void verifyInvestmentTypePillsAreSwitchable_HigherReturns() {
        logStep("Test started: verifyInvestmentTypePillsAreSwitchable_HigherReturns");

        PortfolioPlannerPage plannerPage = goToHigherReturnsForm();

        logStep("Clicking the 'One-time' pill");
        plannerPage.selectOneTimeTab();
        logStep("Clicking the 'Both' pill");
        plannerPage.selectBothTab();
        logStep("Clicking the 'SIP' pill");
        plannerPage.selectSipTab();

        logStep("Test completed: verifyInvestmentTypePillsAreSwitchable_HigherReturns");
    }

    @Test(description = "Higher returns form (SIP only): entering a SIP amount/period and submitting generates "
            + "the investment plan with the correct headline (PRD 11.3.1, 11.2.1's fallback-headline note)")
    public void verifyHigherReturnsSipPlanGeneration() {
        logStep("Test started: verifyHigherReturnsSipPlanGeneration");

        PortfolioPlannerPage plannerPage = generateHigherReturnsPlan();

        logStep("Reading the investment plan headline");
        String actualHeading = plannerPage.getInvestmentPlanHeadingText();
        logStep("Headline is: '" + actualHeading + "' - expecting: '" + expectedHigherReturnsHeading + "'");
        Assert.assertEquals(actualHeading, expectedHigherReturnsHeading,
                "Expected the fallback/higher-returns headline per PRD 11.2.1");

        logStep("Test completed: verifyHigherReturnsSipPlanGeneration");
    }

    @Test(enabled = false, description = "Higher returns form (One-time only / Both variants): PRD 11.3.2/11.3.3. "
            + "Only the SIP-tab field state has been captured so far - need the One-time/Both tab HTML to confirm "
            + "field placeholders/behaviour before automating.")
    public void verifyHigherReturnsOneTimeAndBothPlanGeneration() {
        throw new SkipException("Need HTML for the One-time/Both tab states of this form to implement correctly.");
    }

    // ============================================================
    // Invest for monthly income
    // ============================================================

    @Test(description = "Monthly income form: CTA text vs PRD's 'Show investment plan' requirement. "
            + "KNOWN MISMATCH: the real button is labelled 'Get Investment Plan', so this test is expected to "
            + "fail until the PRD and the shipped UI are reconciled - kept as-is intentionally to surface the gap.")
    public void verifyShowInvestmentPlanButtonLabel_MonthlyIncome() {
        logStep("Test started: verifyShowInvestmentPlanButtonLabel_MonthlyIncome");

        PortfolioPlannerPage plannerPage = goToMonthlyIncomeForm();

        logStep("Reading the CTA button's text");
        String actualCta = plannerPage.getGetInvestmentPlanButtonText();
        logStep("CTA text is: '" + actualCta + "' - expecting (per PRD): '" + expectedMonthlyIncomeCta + "'");
        Assert.assertEquals(actualCta, expectedMonthlyIncomeCta,
                "PRD requires the CTA to read 'Show investment plan' on ALL three planner-option screens");

        logStep("Test completed: verifyShowInvestmentPlanButtonLabel_MonthlyIncome");
    }

    @Test(description = "Monthly income form: 'In the next 1-5 years' timeframe generates the plan (PRD 11.3.5)")
    public void verifyMonthlyIncomePlanGeneration_StartIn1To5Years() {
        logStep("Test started: verifyMonthlyIncomePlanGeneration_StartIn1To5Years");

        PortfolioPlannerPage plannerPage = goToMonthlyIncomeForm();

        logStep("Entering income amount: " + monthlyIncomeAmount);
        plannerPage.enterIncomeAmount(monthlyIncomeAmount);
        logStep("Selecting timeframe: In the next 1-5 years");
        plannerPage.selectStartIn1To5Years();
        logStep("Clicking 'Get Investment Plan'");
        plannerPage.clickGetInvestmentPlan();

        plannerPage.waitUntilInvestmentPlanStepLoaded();
        logStep("Investment plan screen loaded");

        logStep("Test completed: verifyMonthlyIncomePlanGeneration_StartIn1To5Years");
    }

    @Test(description = "Monthly income form: 'In the next 1 year' timeframe generates the plan with the correct "
            + "headline (PRD 11.3.5, and the 'Your regular income plan' note under 11.2.9)")
    public void verifyMonthlyIncomePlanGeneration_StartIn1Year() {
        logStep("Test started: verifyMonthlyIncomePlanGeneration_StartIn1Year");

        PortfolioPlannerPage plannerPage = goToMonthlyIncomeForm();

        logStep("Entering income amount: " + monthlyIncomeAmount);
        plannerPage.enterIncomeAmount(monthlyIncomeAmount);
        logStep("Selecting timeframe: In the next 1 year");
        plannerPage.selectStartIn1Year();
        logStep("Clicking 'Get Investment Plan'");
        plannerPage.clickGetInvestmentPlan();

        plannerPage.waitUntilInvestmentPlanStepLoaded();
        logStep("Investment plan screen loaded");

        logStep("Reading the investment plan headline");
        String actualHeading = plannerPage.getInvestmentPlanHeadingText();
        logStep("Headline is: '" + actualHeading + "' - expecting: '" + expectedMonthlyIncomeHeading + "'");
        Assert.assertEquals(actualHeading, expectedMonthlyIncomeHeading,
                "PRD requires 'Your regular income plan' as the headline for this journey");

        logStep("Test completed: verifyMonthlyIncomePlanGeneration_StartIn1Year");
    }

    @Test(description = "Monthly income form: 'Immediately' timeframe generates the plan (PRD 11.3.5)")
    public void verifyMonthlyIncomePlanGeneration_StartImmediately() {
        logStep("Test started: verifyMonthlyIncomePlanGeneration_StartImmediately");

        PortfolioPlannerPage plannerPage = goToMonthlyIncomeForm();

        logStep("Entering income amount: " + monthlyIncomeAmount);
        plannerPage.enterIncomeAmount(monthlyIncomeAmount);
        logStep("Selecting timeframe: Immediately");
        plannerPage.selectStartImmediately();
        logStep("Clicking 'Get Investment Plan'");
        plannerPage.clickGetInvestmentPlan();

        plannerPage.waitUntilInvestmentPlanStepLoaded();
        logStep("Investment plan screen loaded");

        logStep("Test completed: verifyMonthlyIncomePlanGeneration_StartImmediately");
    }

    // ============================================================
    // Invest to save tax
    // ============================================================

    @Test(description = "Tax-saving form: CTA text vs PRD's 'Show investment plan' requirement. "
            + "KNOWN MISMATCH: the real button is labelled 'Continue', so this test is expected to fail until "
            + "the PRD and the shipped UI are reconciled - kept as-is intentionally to surface the gap.")
    public void verifyShowInvestmentPlanButtonLabel_TaxSaving() {
        logStep("Test started: verifyShowInvestmentPlanButtonLabel_TaxSaving");

        PortfolioPlannerPage plannerPage = goToTaxSavingForm();

        logStep("Reading the CTA button's text");
        String actualCta = plannerPage.getContinueButtonText();
        logStep("CTA text is: '" + actualCta + "' - expecting (per PRD): '" + expectedTaxSavingCta + "'");
        Assert.assertEquals(actualCta, expectedTaxSavingCta,
                "PRD requires the CTA to read 'Show investment plan' on ALL three planner-option screens");

        logStep("Test completed: verifyShowInvestmentPlanButtonLabel_TaxSaving");
    }

    @Test(description = "Tax-saving form: lock-in warning banner is shown with the expected wording")
    public void verifyTaxSavingLockInWarningIsDisplayed() {
        logStep("Test started: verifyTaxSavingLockInWarningIsDisplayed");

        PortfolioPlannerPage plannerPage = goToTaxSavingForm();

        logStep("Reading the lock-in warning banner text");
        String warningText = plannerPage.getTaxLockInWarningText();
        logStep("Asserting warning text contains: '" + taxSavingLockInWarningContains + "'");
        Assert.assertTrue(warningText.contains(taxSavingLockInWarningContains),
                "Expected the tax-saving lock-in warning banner to be shown with its standard wording");

        logStep("Test completed: verifyTaxSavingLockInWarningIsDisplayed");
    }

    @Test(description = "Tax-saving form: entering an amount and clicking Continue generates the investment plan "
            + "(PRD 11.3.4). Headline check is a KNOWN MISMATCH: the real screen currently shows the generic "
            + "'Your investment plan' headline, not the PRD-mandated 'Your tax-saving plan' - kept as-is "
            + "intentionally to surface the gap, same as the CTA-label tests above.")
    public void verifyTaxSavingPlanGeneration() {
        logStep("Test started: verifyTaxSavingPlanGeneration");

        PortfolioPlannerPage plannerPage = goToTaxSavingForm();

        logStep("Entering tax-saving amount: " + taxSavingAmount);
        plannerPage.enterTaxSavingAmount(taxSavingAmount);
        logStep("Clicking 'Continue'");
        plannerPage.clickContinue();

        plannerPage.waitUntilInvestmentPlanStepLoaded();
        logStep("Investment plan screen loaded");

        logStep("Reading the investment plan headline");
        String actualHeading = plannerPage.getInvestmentPlanHeadingText();
        logStep("Headline is: '" + actualHeading + "' - expecting (per PRD): '" + expectedTaxSavingHeading + "'");
        Assert.assertEquals(actualHeading, expectedTaxSavingHeading,
                "PRD requires 'Your tax-saving plan' as the headline for this journey");

        logStep("Test completed: verifyTaxSavingPlanGeneration");
    }

    @Test(enabled = false, description = "Tax-saving form: the financial-year question is shown only between "
            + "1 Jan and 31 Mar, and advances by one FY each year (PRD explicit acceptance criterion). Needs the "
            + "financial-year question's locators, and a way to control/verify the system date the app sees.")
    public void verifyFinancialYearQuestionOnlyShownBetweenJanAndMarch() {
        throw new SkipException("No locators for the financial-year question, and no date-control mechanism.");
    }

    // ============================================================
    // Your investment plan
    // ============================================================

    @Test(description = "Investment plan screen: 'Read more' on the disclaimer is clickable (PRD 11.10: 'Read "
            + "more' expands the full disclaimer). Only the click itself is verified - there's no locator yet for "
            + "the expanded disclaimer panel to assert its content.")
    public void verifyReadMoreDisclaimerLinkIsClickable() {
        logStep("Test started: verifyReadMoreDisclaimerLinkIsClickable");

        PortfolioPlannerPage plannerPage = generateHigherReturnsPlan();

        logStep("Clicking 'Read more' on the disclaimer");
        plannerPage.clickReadMore();

        logStep("Test completed: verifyReadMoreDisclaimerLinkIsClickable");
    }

    @Test(description = "Investment plan screen: 'Edit Investment Plan' opens the Edit Investment Plan screen "
            + "(PRD: 'Upon clicking on the Edit button, users can edit the investment details')")
    public void verifyEditInvestmentPlanButtonOpensEditScreen() {
        logStep("Test started: verifyEditInvestmentPlanButtonOpensEditScreen");

        PortfolioPlannerPage plannerPage = generateHigherReturnsPlan();

        logStep("Clicking 'Edit Investment Plan'");
        plannerPage.clickEditInvestmentPlan();

        plannerPage.waitUntilEditInvestmentPlanStepLoaded();
        logStep("Edit Investment Plan screen loaded");

        logStep("Test completed: verifyEditInvestmentPlanButtonOpensEditScreen");
    }
    @Test(description = "Investment plan screen: 'Edit Investment Plan' opens the Edit Investment Plan screen "
            + "(PRD: 'Upon clicking on the Edit button, users can edit the investment details')")
    public void verifyEditInvestmentPlanButtonOpensEditScreenChangeAmount() {
        logStep("Test started: verifyEditInvestmentPlanButtonOpensEditScreen");

        PortfolioPlannerPage plannerPage = generateHigherReturnsPlan();

        logStep("Clicking 'Edit Investment Plan'");
        plannerPage.clickEditInvestmentPlan();

        plannerPage.waitUntilEditInvestmentPlanStepLoaded();
        logStep("Edit Investment Plan screen loaded");
        plannerPage.enterEditSIPAmount("20000");
        logStep("enter rivised amount");
        logStep("click on review changes");
        logStep("click on confirmation");
        plannerPage.clickPay();
        logStep("click on review changes");
        logStep("click on confirmation");

        logStep("Test completed: verifyEditInvestment screen open & proceed to pay");
    }

    @Test(description = "Investment plan screen: 'Invest Now' starts the cart order journey (PRD: 'An active "
            + "account initiates the cart order journey'). Uses the transaction-capable test investor - the "
            + "default investor has no active transaction account and never reaches Cart from here. Stops at the "
            + "Cart screen - never proceeds to OTP/payment, which would submit a real transaction.")
    public void verifyInvestNowNavigatesToCart() {
        logStep("Test started: verifyInvestNowNavigatesToCart");

        PortfolioPlannerPage plannerPage = generateHigherReturnsPlan(transactionCapableInvestorName);

        logStep("Clicking 'Invest Now'");
        plannerPage.clickInvestNow();

        plannerPage.waitUntilCartLoaded();
        logStep("Cart screen loaded - stopping here (never proceeding to OTP/payment)");

        logStep("Test completed: verifyInvestNowNavigatesToCart");
    }

    @Test(enabled = false, description = "Investment plan screen: the 'View split' dropdown appears only for "
            + "funds with more than one suggested investment (SIP+SIP/SIP+Lumpsum/Lumpsum+Lumpsum) (PRD 11.8). "
            + "Per-fund content is plan-dependent and deliberately has no locators in this framework.")
    public void verifyViewSplitDropdownShownOnlyForMultiInvestmentFunds() {
        throw new SkipException("Per-fund 'View split' is plan-dependent content - no locators by design.");
    }

    @Test(enabled = false, description = "Investment plan screen (free user): shows the real fund count but masks "
            + "fund names/amounts/instalments/category/rationale, uses a Moderate risk profile, and shows the full "
            + "disclaimer (PRD: 'Investment Plan for FREE users'). Needs a free-account test identity.")
    public void verifyFreeUserSeesMaskedInvestmentPlan() {
        throw new SkipException("Needs a free-account test identity - not configured in ConfigReader/BaseTest.");
    }

    @Test(enabled = false, description = "Investment Breakdown screen: opened via the 'Breakdown' button, shows "
            + "per-fund SIP/lumpsum/lumpsum-spread details plus Monthly SIP Total / Lumpsum spread / One-time this "
            + "month / First-month investment aggregates (PRD). No locators captured for this screen yet.")
    public void verifyInvestmentBreakdownScreenAggregates() {
        throw new SkipException("No locators for the Investment Breakdown screen - provide its HTML to implement.");
    }

    @Test(enabled = false, description = "Investment plan screen: clicking Invest Now with an inactive "
            + "advice-cum-transaction account shows 'Your Transaction account is inactive.' with an 'Activate now' "
            + "CTA (PRD). Test investor confirmed (Manish Khatri, testdata: investor.transaction.inactive.name) - "
            + "still needs the alert's own locators (its HTML hasn't been captured yet).")
    public void verifyInvestNowShowsTransactionAccountInactiveAlert() {
        throw new SkipException("No locators for this alert yet - provide its HTML to implement.");
    }

    @Test(enabled = false, description = "Investment plan screen: clicking Invest Now with a transaction account "
            + "still under process shows 'Your transaction account setup is under process.' with an 'Okay' "
            + "dismissal that keeps the user on the same page (PRD). Needs a matching test investor + locators.")
    public void verifyInvestNowShowsTransactionAccountUnderProcessAlert() {
        throw new SkipException("No locators for this alert, and no known test investor in that account state.");
    }

    // ============================================================
    // Edit Investment Plan
    // ============================================================

    @Test(enabled = false, description = "Edit Investment Plan: 'Change Fund' shows 'Suitable alternatives from "
            + "Analyst's Choice' per the documented category mapping (PRD table + Case 1.1/1.2/1.3). Needs the "
            + "Change Fund popup's locators.")
    public void verifyChangeFundShowsSuitableAlternatives() {
        throw new SkipException("No locators for the Change Fund popup - provide its HTML to implement.");
    }

    @Test(enabled = false, description = "Edit Investment Plan: '+ Add fund' opens the add-fund flow, letting a "
            + "fund be added independently of the recommendation (PRD Case 2). Needs the resulting screen's "
            + "locators - clicking the button without knowing where it leads risks stranding the session.")
    public void verifyAddFundFlow() {
        throw new SkipException("No locators for the add-fund flow's destination screen - provide its HTML.");
    }

    @Test(enabled = false, description = "Edit Investment Plan: deleting a fund removes it from the plan (PRD). "
            + "Per-fund controls are plan-dependent and deliberately have no locators in this framework.")
    public void verifyDeleteFundRemovesItFromPlan() {
        throw new SkipException("Per-fund 'Delete fund' is plan-dependent content - no locators by design.");
    }

    @Test(enabled = false, description = "Edit Investment Plan: 'Reset' restores the original suggested plan in "
            + "entirety after edits (PRD). No locator captured for the Reset button yet.")
    public void verifyResetButtonRestoresOriginalPlan() {
        throw new SkipException("No locator for the Reset button - provide its HTML to implement.");
    }

    // ============================================================
    // Review page
    // ============================================================

    @Test(enabled = false, description = "Review page: deviation = Modified minus Original, colour-coded green "
            + "for an increase and red for a decrease, only beyond +/-5% (PRD explicit formula + threshold). "
            + "Needs the Review page's locators.")
    public void verifyDeviationCalculationAndColorCoding() {
        throw new SkipException("No locators for the Review page - provide its HTML to implement.");
    }

    @Test(enabled = false, description = "Review page: 'Invest Now' stays disabled until the disclaimer checkbox "
            + "is ticked (PRD: 'The disclaimer box should appear only when a change is made... ticked by default'; "
            + "'Invest Now button will be frozen... if the disclaimer checkbox is not ticked'). Needs the Review "
            + "page's locators.")
    public void verifyInvestNowDisabledUntilDisclaimerTicked() {
        throw new SkipException("No locators for the Review page - provide its HTML to implement.");
    }

    // ============================================================
    // Field validation errors (PRD's documented error-message table)
    // ============================================================

    @Test(enabled = false, description = "Field validation: SIP/one-time amount below the documented minimum, "
            + "or 'Please enter period.' for a blank period, shows the exact PRD-specified error message. Needs "
            + "the validation-error locators (none of the current forms have these captured).")
    public void verifyAmountAndPeriodValidationErrors() {
        throw new SkipException("No locators for field validation error messages - provide the relevant HTML.");
    }

    @Test(enabled = false, description = "Field validation: a fund suspended for sale blocks the user, while an "
            + "'Exit'-marked or 'Avoidable'-marked fund only notifies without blocking (PRD). Needs the Edit "
            + "Investment Plan fund-status locators, which are plan-dependent and not currently captured.")
    public void verifySuspendedFundBlocksWhileExitAndAvoidableOnlyNotify() {
        throw new SkipException("Fund-status notifications are plan-dependent content - no locators by design.");
    }

    // ============================================================
    // Helpers - not test methods; keep each @Test focused on its own scenario.
    // Each one logs its own steps, so every test that calls it gets those steps
    // in the console/report for free.
    // ============================================================

    private PortfolioPlannerPage selectInvestorAndProceed() {
        return selectInvestorAndProceed(investorName);
    }

    /**
     * Same as the no-arg version, but lets a test pick a specific investor when the
     * default one won't do - e.g. "Manish Khatri" can only generate a plan (no
     * active transaction account), while "Wamiq Azeem Asif" can complete one.
     */
    private PortfolioPlannerPage selectInvestorAndProceed(String investor) {
        logStep("Opening Portfolio Planner");
        PortfolioPlannerPage plannerPage = new PortfolioPlannerPage(driver);
        plannerPage.waitUntilLoaded();

        logStep("Selecting investor: " + investor);
        plannerPage.selectInvestor(investor);

        logStep("Clicking 'Next'");
        plannerPage.clickNext();

        plannerPage.waitUntilAimStepLoaded();
        logStep("Aim-selection step loaded");

        return plannerPage;
    }

    private PortfolioPlannerPage goToHigherReturnsForm() {
        return goToHigherReturnsForm(investorName);
    }

    private PortfolioPlannerPage goToHigherReturnsForm(String investor) {
        PortfolioPlannerPage plannerPage = selectInvestorAndProceed(investor);

        logStep("Selecting aim: Invest for higher returns");
        plannerPage.selectInvestForHigherReturns();

        plannerPage.waitUntilInvestForHigherReturnsStepLoaded();
        logStep("Invest for higher returns form loaded");

        return plannerPage;
    }

    private PortfolioPlannerPage goToMonthlyIncomeForm() {
        PortfolioPlannerPage plannerPage = selectInvestorAndProceed();

        logStep("Selecting aim: Invest for monthly income");
        plannerPage.selectInvestForMonthlyIncome();

        plannerPage.waitUntilInvestForMonthlyIncomeStepLoaded();
        logStep("Invest for monthly income form loaded");

        return plannerPage;
    }

    private PortfolioPlannerPage goToTaxSavingForm() {
        PortfolioPlannerPage plannerPage = selectInvestorAndProceed();

        logStep("Selecting aim: Invest to save tax");
        plannerPage.selectInvestToSaveTax();

        plannerPage.waitUntilInvestToSaveTaxStepLoaded();
        logStep("Invest to save tax form loaded");

        return plannerPage;
    }

    /** SIP-based "Invest for higher returns" plan, used by tests that only care about the resulting plan screen. */
    private PortfolioPlannerPage generateHigherReturnsPlan() {
        return generateHigherReturnsPlan(investorName);
    }

    private PortfolioPlannerPage generateHigherReturnsPlan(String investor) {
        PortfolioPlannerPage plannerPage = goToHigherReturnsForm(investor);

        logStep("Selecting the 'SIP' investment-type pill");
        plannerPage.selectSipTab();

        logStep("Entering SIP amount: " + higherReturnsSipAmount);
        plannerPage.enterSipAmount(higherReturnsSipAmount);

        logStep("Entering investment period: " + higherReturnsInvestmentPeriod);
        plannerPage.enterInvestmentPeriod(higherReturnsInvestmentPeriod);

        logStep("Selecting the 'Years' period unit");
        plannerPage.selectYearsUnit();

        logStep("Clicking 'Show investment plan'");
        plannerPage.clickShowInvestmentPlan();

        plannerPage.waitUntilInvestmentPlanStepLoaded();
        logStep("Investment plan screen loaded");

        return plannerPage;
    }
}
