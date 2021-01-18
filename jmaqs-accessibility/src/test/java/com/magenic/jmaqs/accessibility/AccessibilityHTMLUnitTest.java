/*
 * Copyright 2021 (C) Magenic, All rights Reserved
 */

package com.magenic.jmaqs.accessibility;

import com.deque.html.axecore.results.AxeRuntimeException;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.ResultType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magenic.jmaqs.selenium.BaseSeleniumTest;
import com.magenic.jmaqs.selenium.LazyWebElement;
import com.magenic.jmaqs.selenium.SeleniumConfig;
import com.magenic.jmaqs.selenium.UIWait;
import com.magenic.jmaqs.selenium.factories.UIWaitFactory;
import com.magenic.jmaqs.utilities.helper.TestCategories;
import com.magenic.jmaqs.utilities.logging.FileLogger;
import com.magenic.jmaqs.utilities.logging.MessageType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

public class AccessibilityHTMLUnitTest extends BaseSeleniumTest {
  ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Axe JSON with an error.
   */
  private static final String axeResultWithError = "{\"errorMessage\": \"AutomationError\",\"results\":{ \"toolOptions\":{\"reporter\":\"v1\" }, \"testEngine\": {\"name\":\"axe-core\",\"version\":\"3.4.1\" }, \"testEnvironment\": {\"userAgent\":\"AutoAgent\",\"windowWidth\": 1200,\"windowHeight\": 646,\"orientationAngle\": 0,\"orientationType\":\"landscape-primary\" }, \"testRunner\": {\"name\":\"axe\" }, \"url\":\"url\", \"timestamp\":\"2020-04-14T01:33:59.139Z\", \"passes\":[], \"violations\":[], \"incomplete\":[], \"inapplicable\": []}}";

  /**
   * Unit testing site URL - Login page.
   */
  private static final String TestSiteUrl = SeleniumConfig.getWebSiteBase();

  /**
   * Unit testing site URL - Automation page.
   */
  private static final String TestSiteAutomationUrl = TestSiteUrl + "Automation/";

  /**
   * Sets up the tests and navigates to the test site.
   */
  @BeforeMethod
  public void setup() {
    getWebDriver().navigate().to(TestSiteUrl);
    UIWait wait = UIWaitFactory.getWaitDriver(getWebDriver());
    wait.waitForPageLoad();
  }

  /**
   * Verify we can create and associate an accessibility HTML report.
   * @throws IOException if an exception is thrown
   */
  @Test(groups = TestCategories.ACCESSIBILITY)
  public void AccessibilityHtmlReport() throws IOException, ParseException {
    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(), false);

    String filePath = Arrays.stream(this.getTestObject().getArrayOfAssociatedFiles())
        .filter(x -> x.contains(".html")).findFirst().map(Object::toString).orElse("");
    Assert.assertTrue(filePath.length() > 0, "Accessibility report is empty");

    deleteFiles(Collections.singletonList(filePath));
  }

  /**
   * Verify we can create and associate multiple accessibility HTML reports.
   * @throws IOException if an exception is thrown
   */
  @Test(groups = TestCategories.ACCESSIBILITY)
  public void AccessibilityMultipleHtmlReports() throws IOException, ParseException {
    // Create 3 reports
    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(), false);
    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(), false);
    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(), false);

    long count = Arrays.stream(this.getTestObject().getArrayOfAssociatedFiles())
        .filter(x -> x.contains(".html")).count();
    Assert.assertEquals(count, 3,
        "Expected 3 accessibility reports but see " + count + " instead");
    String[] reports = this.getTestObject().getArrayOfAssociatedFiles();

    deleteFiles(Arrays.asList(reports.clone()));
  }

  /**
   * Verify we throw an exception if the scan has an error.
   */
  // TODO: May not need this test because of error tests in AXE repository
  @Ignore @Test(groups = TestCategories.ACCESSIBILITY, expectedExceptions = AxeRuntimeException.class)
  public void AccessibilityHtmlReportWithError() throws IOException, ParseException {
    Results results = objectMapper.readValue(axeResultWithError, Results.class);
    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(), results, false);

    String filePath = Arrays.stream(this.getTestObject().getArrayOfAssociatedFiles())
        .filter(x -> x.contains(".html")).findFirst().map(Object::toString).orElse("");
    Assert.assertTrue(filePath.length() > 0, "Accessibility report is empty");

    deleteFiles(Collections.singletonList(filePath));
  }

  /**
   * Verify we throw an exception if the scan has an error and are using lazy elements.
   */
  @Ignore @Test(groups = TestCategories.ACCESSIBILITY, expectedExceptions = AxeRuntimeException.class)
  public void AccessibilityHtmlReportWithErrorFromLazyElement() throws IOException, ParseException {
    getWebDriver().navigate().to(TestSiteAutomationUrl);
    UIWait wait = UIWaitFactory.getWaitDriver(getWebDriver());
    wait.waitForPageLoad();

    Results error = objectMapper.convertValue(axeResultWithError, Results.class);
    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(), error,false);

    String filePath = Arrays.stream(this.getTestObject().getArrayOfAssociatedFiles())
        .filter(x -> x.contains(".html")).findFirst().map(Object::toString).orElse("");

    Assert.assertTrue(filePath.length() > 0, "Accessibility report is empty");
    deleteFiles(Collections.singletonList(filePath));
  }

  /**
   * Verify we throw an exception if there are violations and we choose the throw exception option.
   * @throws IOException if an exception is thrown
   */
  @Test(groups = TestCategories.ACCESSIBILITY, expectedExceptions = AxeRuntimeException.class)
  public void AccessibilityHtmlReportWithViolation() throws IOException, ParseException {
    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(), true);
  }

  /**
   * Verify we can create an accessibility HTML report off a lazy element.
   * @throws IOException if exception is thrown
   */
  @Test(groups = TestCategories.ACCESSIBILITY)
  public void AccessibilityHtmlReportWithLazyElement() throws IOException, ParseException {
    getWebDriver().get(TestSiteAutomationUrl);
    UIWait wait = UIWaitFactory.getWaitDriver(getWebDriver());
    wait.waitForPageLoad();

    LazyWebElement foodTable = new LazyWebElement(this.getTestObject(),
        By.id("FoodTable"), "Food Table");

    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(),
        foodTable.getRawExistingElement(), false);

    String filePath = Arrays.stream(this.getTestObject().getArrayOfAssociatedFiles())
        .filter(x -> x.contains(".html")).findFirst().map(Object::toString).orElse("");
    Assert.assertFalse(filePath.isEmpty(), "Accessibility report is empty");
  }

  /**
   *  Verify we can create an accessibility HTML report off a normal web element.
   * @throws IOException if exception is thrown
   */
  @Test(groups = TestCategories.ACCESSIBILITY)
  public void AccessibilityHtmlReportWithElement() throws IOException, ParseException {
    getWebDriver().navigate().to(TestSiteAutomationUrl);
    UIWait wait = UIWaitFactory.getWaitDriver(getWebDriver());
    wait.waitForPageLoad();

    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(),
        this.getWebDriver().findElement(By.id("FoodTable")), false);

    String filePath = Arrays.stream(this.getTestObject().getArrayOfAssociatedFiles())
        .filter(x -> x.contains(".html")).findFirst().map(Object::toString).orElse("");

    File file = new File(filePath);
    FileInputStream fis = new FileInputStream(file);
    String fileString = IOUtils.toString(fis, StandardCharsets.UTF_8);
    Assert.assertFalse(fileString.isEmpty(), "Accessibility report is empty");
  }

  /**
   * Verify we suppress the JS logging associated with running Axe.
   * @throws IOException if an exception is thrown
   */
  @Test(groups = TestCategories.ACCESSIBILITY)
  public void AccessibilityHtmlLogSuppression() throws IOException, ParseException {
    // Make sure we are not using verbose logging
    this.getLogger().setLoggingLevel(MessageType.INFORMATION);

    getWebDriver().navigate().to(TestSiteAutomationUrl);
    UIWait wait = UIWaitFactory.getWaitDriver(getWebDriver());
    wait.waitForPageLoad();

    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(), false);

    // The script executed message should be suppressed when we run the accessibility check
    FileInputStream fis = new FileInputStream(((FileLogger)this.getLogger()).getFilePath());
    String file = IOUtils.toString(fis, StandardCharsets.UTF_8);

    Assert.assertFalse(file.contains("Script executed"), "Logging was not suppressed as expected.");
    deleteFiles(Collections.singletonList(file));
  }

  /**
   * Verify we create an HTML file with only the violations.
   * @throws IOException if an IO exception occurs
   * @throws ParseException if a parsing exception occurs
   */
  @Test(groups = TestCategories.ACCESSIBILITY)
  public void AccessibilityHtmlReportViolationsOnly() throws IOException, ParseException {
    // Make sure we are not using verbose logging
    this.getLogger().setLoggingLevel(MessageType.INFORMATION);

    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(),
        false, Collections.singletonList(ResultType.Violations));

    // The script executed message should be suppressed when we run the accessibility check
    FileInputStream fis = new FileInputStream(((FileLogger)this.getLogger()).getFilePath());
    String fileString = IOUtils.toString(fis, StandardCharsets.UTF_8);
    Assert.assertFalse(fileString.contains("Passes "), "Passes were still in the report");
    Assert.assertFalse(fileString.contains("Inapplicable "), "Inapplicable were still in the report");
    Assert.assertFalse(fileString.contains("Incomplete  "), "Incomplete were still in the report");
  }

  /**
   *  Verify we can create an accessibility HTML report off a normal web element.
   * @throws IOException if exception is thrown
   */
  @Test(groups = TestCategories.ACCESSIBILITY)
  public void AccessibilityHtmlViolationsReportWithElement() throws IOException, ParseException {
    getWebDriver().navigate().to(TestSiteAutomationUrl);
    UIWait wait = UIWaitFactory.getWaitDriver(getWebDriver());
    wait.waitForPageLoad();

    AccessibilityUtilities.createAccessibilityHtmlReport(this.getTestObject(),
        this.getWebDriver().findElement(By.id("FoodTable")),
        false, Collections.singletonList(ResultType.Violations));

    String filePath = Arrays.stream(this.getTestObject().getArrayOfAssociatedFiles())
        .filter(x -> x.contains(".html")).findFirst().map(Object::toString).orElse("");
    Assert.assertFalse(filePath.isEmpty(), "Accessibility report is empty");
    deleteFiles(Collections.singletonList(filePath));
  }

  private void deleteFiles(List<String> files) {
    for (String file : files) {
      File filePath = new File(file);
      Assert.assertTrue(filePath.exists(),
          System.lineSeparator() + "File does not exist: " + file + System.lineSeparator());
      Assert.assertTrue(filePath.delete(),
          System.lineSeparator() +"File was not deleted: " + file + System.lineSeparator());
    }
  }
}