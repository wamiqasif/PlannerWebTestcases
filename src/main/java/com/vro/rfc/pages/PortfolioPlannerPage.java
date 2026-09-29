package com.vro.rfc.pages;

import com.vro.rfc.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Portfolio Planner - investor selection, risk profile, and investing-aim steps.
 *
 * The page has no id/data-testid/name attributes to key off - every class here is
 * a Tailwind utility (often with arbitrary values like border-[2px]) that is
 * expected to churn with restyling. Locators are anchored on stable, human-facing
 * text instead, which is the next-best option per the framework's locator
 * priority. The exceptions are the "Next" button (data-slot="button" attribute)
 * and the aim cards (their alt/h3 text doubles as a stable content key since
 * there are only ever these three fixed aims).
 */
public class PortfolioPlannerPage extends BasePage {

    @FindBy(xpath = "//h1[normalize-space()='Portfolio Planner']")
    private WebElement pageHeading;

    /** Anchors the investor button list: they are this paragraph's next sibling's children. */
    private static final String INVESTOR_LIST_ANCHOR_XPATH =
            "//p[normalize-space()='Choose one account to get started']/following-sibling::div[1]";

    @FindBy(xpath = "//button[normalize-space()='+ Add another investor']")
    private WebElement addAnotherInvestorButton;

    @FindBy(xpath = "//span[starts-with(normalize-space(),'Risk profile:')]")
    private WebElement riskProfileLabel;

    @FindBy(xpath = "//span[starts-with(normalize-space(),'Risk profile:')]/following-sibling::button[normalize-space()='Update']")
    private WebElement updateRiskProfileButton;

    /** Date in the sentence changes per user - matched on the stable leading text only. */
    @FindBy(xpath = "//p[contains(normalize-space(),'As per your last risk assessment dated')]")
    private WebElement riskAssessmentDateText;

    @FindBy(xpath = "//button[@data-slot='button' and normalize-space()='Next']")
    private WebElement nextButton;

    // --- "Choose an investing aim" step ---

    @FindBy(xpath = "//button[contains(normalize-space(),'Back')]")
    private WebElement backButton;

    @FindBy(xpath = "//h1[normalize-space()='Choose an investing aim']")
    private WebElement chooseAimHeading;

    /** e.g. "Manish Khatri" - the bold name inside the "Investor Selected: <b>" line. */
    @FindBy(xpath = "//div[contains(normalize-space(),'Investor Selected:')]/b")
    private WebElement selectedInvestorName;

    @FindBy(xpath = "//button[.//h3[normalize-space()='Invest for higher returns']]")
    private WebElement investForHigherReturnsCard;

    @FindBy(xpath = "//button[.//h3[normalize-space()='Invest for monthly income']]")
    private WebElement investForMonthlyIncomeCard;

    @FindBy(xpath = "//button[.//h3[normalize-space()='Invest to save tax']]")
    private WebElement investToSaveTaxCard;

    // --- "Invest for higher returns on savings" (SIP details) step ---

    @FindBy(xpath = "//h1[normalize-space()='Invest for higher returns on savings']")
    private WebElement investForHigherReturnsHeading;

    /** role/aria-label are real accessibility attributes here, not styling - stable. */
    private static final String INVESTMENT_TYPE_TABLIST_XPATH =
            "//div[@role='tablist' and @aria-label='Investment type']";

    @FindBy(xpath = INVESTMENT_TYPE_TABLIST_XPATH + "/button[normalize-space()='SIP']")
    private WebElement sipTab;

    @FindBy(xpath = INVESTMENT_TYPE_TABLIST_XPATH + "/button[normalize-space()='One-time']")
    private WebElement oneTimeTab;

    @FindBy(xpath = INVESTMENT_TYPE_TABLIST_XPATH + "/button[normalize-space()='Both']")
    private WebElement bothTab;

    @FindBy(css = "input[placeholder='Enter SIP Amount']")
    private WebElement sipAmountInput;

    @FindBy(css = "input[placeholder='Enter Period']")
    private WebElement investmentPeriodInput;

    private static final String PERIOD_UNIT_TABLIST_XPATH =
            "//div[@role='tablist' and @aria-label='How long do you want to invest for?']";

    @FindBy(xpath = PERIOD_UNIT_TABLIST_XPATH + "/button[normalize-space()='Months']")
    private WebElement monthsUnitTab;

    @FindBy(xpath = PERIOD_UNIT_TABLIST_XPATH + "/button[normalize-space()='Years']")
    private WebElement yearsUnitTab;

    @FindBy(xpath = "//button[@data-slot='button' and normalize-space()='Show investment plan']")
    private WebElement showInvestmentPlanButton;

    // --- "Invest for monthly income" (income details) step ---

    @FindBy(xpath = "//h1[normalize-space()='Invest for monthly income']")
    private WebElement investForMonthlyIncomeHeading;

    @FindBy(css = "input[placeholder='Enter Amount']")
    private WebElement incomeAmountInput;

    /** No shared wrapper attribute here (unlike the SIP tabs) - matched on their own fixed text. */
    @FindBy(xpath = "//button[normalize-space()='In the next 1-5 years']")
    private WebElement startIn1To5YearsButton;

    @FindBy(xpath = "//button[normalize-space()='In the next 1 year']")
    private WebElement startIn1YearButton;

    @FindBy(xpath = "//button[normalize-space()='Immediately']")
    private WebElement startImmediatelyButton;

    @FindBy(xpath = "//button[@data-slot='button' and normalize-space()='Get Investment Plan']")
    private WebElement getInvestmentPlanButton;

    // --- "Invest to save tax" (tax details) step ---

    @FindBy(xpath = "//h1[normalize-space()='Invest to save tax']")
    private WebElement investToSaveTaxHeading;

    @FindBy(xpath = "//div[contains(normalize-space(),'Tax-saving funds have a lock-in period of 3 years')]")
    private WebElement taxLockInWarningBanner;

    /** Same placeholder as the income step's field, but a distinct field/name for business clarity. */
    @FindBy(css = "input[placeholder='Enter Amount']")
    private WebElement taxSavingAmountInput;

    @FindBy(xpath = "//button[normalize-space()='Help me calculate this']")
    private WebElement helpMeCalculateButton;

    @FindBy(xpath = "//button[@data-slot='button' and normalize-space()='Continue']")
    private WebElement continueButton;

    // --- "Your investment plan" step ---
    // Only the main heading and the static, plan-independent buttons are covered here -
    // fund name/amount/duration/reasoning text all change per generated plan and are
    // deliberately left out.

    /**
     * NOT matched on literal text - PRD 11.2 says this headline changes per goal
     * ("Your investment plan" / "Your regular income plan" / "Your tax-saving plan" /
     * etc). Anchored structurally instead: it's the h1 immediately after the
     * "Investor Selected: <name>" line, which is present and in the same position
     * for every goal variant.
     */
    @FindBy(xpath = "//div[contains(normalize-space(),'Investor Selected:')]/following-sibling::h1[1]")
    private WebElement investmentPlanHeading;

    @FindBy(xpath = "//button[normalize-space()='Breakdown']")
    private WebElement breakdownButton;

    @FindBy(xpath = "//button[normalize-space()='Read more']")
    private WebElement readMoreButton;

    /**
     * Desktop-panel "Edit Investment Plan" button. Its data-slot='button' attribute plus
     * exact text uniquely identifies it - the mobile-only sticky-bar duplicate is an
     * icon button with an aria-label instead, no data-slot, so it never matches this.
     */
    private static final String EDIT_INVESTMENT_PLAN_BUTTON_XPATH =
            "//button[@data-slot='button' and normalize-space()='Edit Investment Plan']";

    @FindBy(xpath = EDIT_INVESTMENT_PLAN_BUTTON_XPATH)
    private WebElement editInvestmentPlanButton;

    /**
     * Desktop-panel "Invest now" button. Both the desktop panel and the mobile sticky
     * bar render a data-slot='button' "Invest now" - scoped here as the sibling
     * immediately preceding the (uniquely identifiable) desktop "Edit Investment Plan"
     * button to avoid matching the mobile duplicate. The mobile sticky bar itself is
     * CSS-hidden at the desktop viewport this framework runs at, so it's out of scope.
     */
    @FindBy(xpath = EDIT_INVESTMENT_PLAN_BUTTON_XPATH + "/preceding-sibling::button[@data-slot='button' and normalize-space()='Invest now']")
    private WebElement investNowButton;

    // --- "Edit Investment Plan" step ---
    // Only the main heading and the static, plan-independent buttons are covered here -
    // the fund count, and every per-fund control (Change Fund/Hide/+Add SIP/+Add
    // one-time/Delete fund/amounts) repeat once per fund and change with the plan, so
    // they're deliberately left out.

    @FindBy(xpath = "//h1[normalize-space()='Edit Investment Plan']")
    private WebElement editInvestmentPlanPageHeading;

    @FindBy(xpath = "//button[normalize-space()='+ Add fund']")
    private WebElement addFundButton;

    @FindBy(xpath = "//button[@data-slot='button' and normalize-space()='Exit']")
    private WebElement exitEditPlanButton;

    /** Named distinctly from investNowButton (the "Your investment plan" step's own button). */
    @FindBy(xpath = "//button[@data-slot='button' and normalize-space()='Invest Now']")
    private WebElement confirmInvestNowButton;

    // --- Cart / Checkout flow ---
    // Unlike the wizard steps above, this markup is a Bootstrap-based screen with real
    // id attributes throughout, so most locators below use @FindBy(id=...) directly -
    // the most stable option available. Still excluded, per plan/cart-dependent content:
    // the per-fund accordion cards (name/SIP badge/amount, repeat once per cart item),
    // the investor tabs (repeat once per investor in the cart), the OTP digit inputs
    // (not buttons/headings), and all amount figures.

    @FindBy(id = "back-to-port-builder")
    private WebElement backToPortfolioBuilderButton;

    /** "Your Cart (N)" - matched by class, not text, since the count is dynamic. */
    @FindBy(css = "h1.cart-title")
    private WebElement cartTitleHeading;

    @FindBy(id = "cart-pay-btn")
    private WebElement proceedToPayButton;

    @FindBy(id = "cart-pay-btn-mob")
    private WebElement proceedToPayButtonMobile;

    @FindBy(id = "back-to-cart")
    private WebElement backToCartButton;

    @FindBy(css = "h4.authorize-heading")
    private WebElement authorizeOtpHeading;

    /** class="resend" - the plain-SIP-flow resend button (distinct from resend-nominee-otp below). */
    @FindBy(css = "button.resend")
    private WebElement resendOtpButton;

    @FindBy(css = "button.resend-nominee-otp")
    private WebElement resendNomineeOtpButton;

    @FindBy(id = "validate-otp-sip")
    private WebElement authorizeSipButton;

    @FindBy(id = "validate-otp-lumpsum")
    private WebElement authorizeLumpsumButton;

    @FindBy(id = "validate-otp-cart")
    private WebElement authorizeCartButton;

    @FindBy(id = "validate-otp-nominee")
    private WebElement authorizeNomineeButton;

    @FindBy(id = "payment-link-button")
    private WebElement payButton;

    @FindBy(xpath = "//h4[normalize-space()='Payment Request Sent']")
    private WebElement paymentRequestSentHeading;

    /** class="title" alone is too generic app-wide, so matched on its fixed text instead. */
    @FindBy(xpath = "//h3[normalize-space()='Auto-debit mandate']")
    private WebElement autoDebitMandateHeading;

    @FindBy(id = "approve-mandate-link")
    private WebElement createMandateButton;

    @FindBy(css = "h6.order-summary-heading")
    private WebElement orderSummaryHeading;

    @FindBy(xpath = "//a[contains(@class,'action-btn') and normalize-space()='OK']")
    private WebElement orderSuccessOkButton;

    @FindBy(xpath = "//a[contains(@class,'action-btn') and normalize-space()='Retry']")
    private WebElement orderFailedRetryButton;

    @FindBy(xpath = "//a[contains(@class,'action-btn') and normalize-space()='Create auto-debit mandate']")
    private WebElement createAutoDebitMandateLink;

    @FindBy(xpath = "//a[normalize-space()='Orders page']")
    private WebElement ordersPageLink;

    @FindBy(xpath = "//h5[normalize-space()='Cancel Transaction?']")
    private WebElement cancelTransactionHeading;

    @FindBy(id = "close-sell-widget")
    private WebElement cancelTransactionYesButton;

    /** No id on this one - scoped to the (id-bearing) modal it lives in. */
    @FindBy(xpath = "//div[@id='close-transaction-side-panel']//button[normalize-space()='No']")
    private WebElement cancelTransactionNoButton;

    public PortfolioPlannerPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilLoaded() {
        wait.waitForVisible(pageHeading);
    }

    /** Selects an investor from the account list by its exact displayed name. */
    public void selectInvestor(String investorName) {
        By locator = By.xpath(INVESTOR_LIST_ANCHOR_XPATH + "/button[normalize-space()=" + xpathLiteral(investorName) + "]");
        wait.waitForClickable(locator).click();
    }

    public void clickAddAnotherInvestor() {
        wait.waitForClickable(addAnotherInvestorButton).click();
    }

    public String getRiskProfileLabelText() {
        return wait.waitForVisible(riskProfileLabel).getText();
    }

    public void clickUpdateRiskProfile() {
        wait.waitForClickable(updateRiskProfileButton).click();
    }

    public String getRiskAssessmentDateText() {
        return wait.waitForVisible(riskAssessmentDateText).getText();
    }

    public void clickNext() {
        wait.waitForClickable(nextButton).click();
    }

    // --- "Choose an investing aim" step ---

    public void waitUntilAimStepLoaded() {
        wait.waitForVisible(chooseAimHeading);
    }

    public void clickBack() {
        wait.waitForClickable(backButton).click();
    }

    public String getSelectedInvestorName() {
        return wait.waitForVisible(selectedInvestorName).getText();
    }

    public void selectInvestForHigherReturns() {
        wait.waitForClickable(investForHigherReturnsCard).click();
    }

    public void selectInvestForMonthlyIncome() {
        wait.waitForClickable(investForMonthlyIncomeCard).click();
    }

    public void selectInvestToSaveTax() {
        wait.waitForClickable(investToSaveTaxCard).click();
    }

    // --- "Invest for higher returns on savings" (SIP details) step ---

    public void waitUntilInvestForHigherReturnsStepLoaded() {
        wait.waitForVisible(investForHigherReturnsHeading);
    }

    public void selectSipTab() {
        wait.waitForClickable(sipTab).click();
    }

    public void selectOneTimeTab() {
        wait.waitForClickable(oneTimeTab).click();
    }

    public void selectBothTab() {
        wait.waitForClickable(bothTab).click();
    }

    public void enterSipAmount(String amount) {
        WebElement field = wait.waitForVisible(sipAmountInput);
        field.clear();
        field.sendKeys(amount);
    }

    public void enterInvestmentPeriod(String period) {
        WebElement field = wait.waitForVisible(investmentPeriodInput);
        field.clear();
        field.sendKeys(period);
    }

    public void selectMonthsUnit() {
        wait.waitForClickable(monthsUnitTab).click();
    }

    public void selectYearsUnit() {
        wait.waitForClickable(yearsUnitTab).click();
    }

    public void clickShowInvestmentPlan() {
        wait.waitForClickable(showInvestmentPlanButton).click();
    }

    public String getShowInvestmentPlanButtonText() {
        return wait.waitForVisible(showInvestmentPlanButton).getText();
    }

    // --- "Invest for monthly income" (income details) step ---

    public void waitUntilInvestForMonthlyIncomeStepLoaded() {
        wait.waitForVisible(investForMonthlyIncomeHeading);
    }

    public void enterIncomeAmount(String amount) {
        WebElement field = wait.waitForVisible(incomeAmountInput);
        field.clear();
        field.sendKeys(amount);
    }

    public void selectStartIn1To5Years() {
        wait.waitForClickable(startIn1To5YearsButton).click();
    }

    public void selectStartIn1Year() {
        wait.waitForClickable(startIn1YearButton).click();
    }

    public void selectStartImmediately() {
        wait.waitForClickable(startImmediatelyButton).click();
    }

    public void clickGetInvestmentPlan() {
        wait.waitForClickable(getInvestmentPlanButton).click();
    }

    public String getGetInvestmentPlanButtonText() {
        return wait.waitForVisible(getInvestmentPlanButton).getText();
    }

    // --- "Invest to save tax" (tax details) step ---

    public void waitUntilInvestToSaveTaxStepLoaded() {
        wait.waitForVisible(investToSaveTaxHeading);
    }

    public String getTaxLockInWarningText() {
        return wait.waitForVisible(taxLockInWarningBanner).getText();
    }

    public void enterTaxSavingAmount(String amount) {
        WebElement field = wait.waitForVisible(taxSavingAmountInput);
        field.clear();
        field.sendKeys(amount);
    }

    public void clickHelpMeCalculate() {
        wait.waitForClickable(helpMeCalculateButton).click();
    }

    public void clickContinue() {
        wait.waitForClickable(continueButton).click();
    }

    public String getContinueButtonText() {
        return wait.waitForVisible(continueButton).getText();
    }

    // --- "Your investment plan" step ---

    public void waitUntilInvestmentPlanStepLoaded() {
        wait.waitForVisible(investmentPlanHeading);
    }

    /** Text varies by goal per PRD 11.2 - use this to assert the goal-specific headline. */
    public String getInvestmentPlanHeadingText() {
        return wait.waitForVisible(investmentPlanHeading).getText();
    }

    public void clickBreakdown() {
        wait.waitForClickable(breakdownButton).click();
    }

    public void clickReadMore() {
        wait.waitForClickable(readMoreButton).click();
    }

    public void clickEditInvestmentPlan() {
        wait.waitForClickable(editInvestmentPlanButton).click();
    }

    public void clickInvestNow() {
        wait.waitForClickable(investNowButton).click();
    }

    // --- "Edit Investment Plan" step ---

    public void waitUntilEditInvestmentPlanStepLoaded() {
        wait.waitForVisible(editInvestmentPlanPageHeading);
    }

    public void clickAddFund() {
        wait.waitForClickable(addFundButton).click();
    }

    public void clickExitEditPlan() {
        wait.waitForClickable(exitEditPlanButton).click();
    }

    public void clickConfirmInvestNow() {
        wait.waitForClickable(confirmInvestNowButton).click();
    }

    // --- Cart / Checkout flow ---

    /**
     * Cross-app navigation (this SPA -> the separate legacy cart app), so allow
     * more time than the default explicit wait in case it's just a slower
     * full-page transition. If this still times out, it's more likely that
     * "Invest Now" actually landed on a transaction-account alert instead of the
     * cart - per PRD, the account may be inactive or still under process for this
     * investor - not a locator bug. That screen isn't automated yet (no locators).
     */
    public void waitUntilCartLoaded() {
        new WaitUtils(driver, 30).waitForVisible(cartTitleHeading);
    }

    public void clickBackToPortfolioBuilder() {
        wait.waitForClickable(backToPortfolioBuilderButton).click();
    }

    public void clickProceedToPay() {
        wait.waitForClickable(proceedToPayButton).click();
    }

    public void clickProceedToPayMobile() {
        wait.waitForClickable(proceedToPayButtonMobile).click();
    }

    public void clickBackToCart() {
        wait.waitForClickable(backToCartButton).click();
    }

    public void waitUntilAuthorizeOtpStepLoaded() {
        wait.waitForVisible(authorizeOtpHeading);
    }

    public void clickResendOtp() {
        wait.waitForClickable(resendOtpButton).click();
    }

    public void clickResendNomineeOtp() {
        wait.waitForClickable(resendNomineeOtpButton).click();
    }

    public void clickAuthorizeSip() {
        wait.waitForClickable(authorizeSipButton).click();
    }

    public void clickAuthorizeLumpsum() {
        wait.waitForClickable(authorizeLumpsumButton).click();
    }

    public void clickAuthorizeCart() {
        wait.waitForClickable(authorizeCartButton).click();
    }

    public void clickAuthorizeNominee() {
        wait.waitForClickable(authorizeNomineeButton).click();
    }

    public void clickPay() {
        wait.waitForClickable(payButton).click();
    }

    public void waitUntilPaymentRequestSentLoaded() {
        wait.waitForVisible(paymentRequestSentHeading);
    }

    public void waitUntilAutoDebitMandateStepLoaded() {
        wait.waitForVisible(autoDebitMandateHeading);
    }

    public void clickCreateMandate() {
        wait.waitForClickable(createMandateButton).click();
    }

    public void waitUntilOrderSummaryLoaded() {
        wait.waitForVisible(orderSummaryHeading);
    }

    public void clickOrderSuccessOk() {
        wait.waitForClickable(orderSuccessOkButton).click();
    }

    public void clickOrderFailedRetry() {
        wait.waitForClickable(orderFailedRetryButton).click();
    }

    public void clickCreateAutoDebitMandateLink() {
        wait.waitForClickable(createAutoDebitMandateLink).click();
    }

    public void clickOrdersPageLink() {
        wait.waitForClickable(ordersPageLink).click();
    }

    public void waitUntilCancelTransactionModalLoaded() {
        wait.waitForVisible(cancelTransactionHeading);
    }

    public void clickCancelTransactionYes() {
        wait.waitForClickable(cancelTransactionYesButton).click();
    }

    public void clickCancelTransactionNo() {
        wait.waitForClickable(cancelTransactionNoButton).click();
    }

    /**
     * Builds a safe XPath string literal for a value that may itself contain a
     * single quote (e.g. an investor name like "D'Souza").
     */
    private static String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        String[] parts = value.split("'", -1);
        StringBuilder sb = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            sb.append("'").append(parts[i]).append("'");
            if (i != parts.length - 1) {
                sb.append(", \"'\", ");
            }
        }
        return sb.append(")").toString();
    }
}
