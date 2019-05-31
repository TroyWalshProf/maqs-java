/* 
 * Copyright 2017 (C) Magenic, All rights Reserved
 */

package com.magenic.jmaqs.selenium.baseSeleniumTest;

import com.magenic.jmaqs.baseTest.BaseTest;
import com.magenic.jmaqs.utilities.helper.StringProcessor;
import com.magenic.jmaqs.utilities.logging.Logger;
import com.magenic.jmaqs.utilities.logging.LoggingEnabled;
import com.magenic.jmaqs.utilities.logging.MessageType;

import com.magenic.jmaqs.utilities.logging.TestResultType;
import org.openqa.selenium.WebDriver;
import org.testng.ITest;
import org.testng.ITestResult;

/**
 * Base Selenium Test class.
 */
public abstract class BaseSeleniumTest extends BaseTest {

  /**
   * Initialize a new instance of the BaseSeleniumTest class.
   */
  public BaseSeleniumTest() {
  }

  /**
   * Thread local storage of SeleniumTestObject.
   */
  private ThreadLocal<SeleniumTestObject> seleniumTestObject = new ThreadLocal<SeleniumTestObject>();

  /**
   * Get WebDriver.
   * 
   * @return WebDriver
   */
  public WebDriver getWebDriver() {
    return this.seleniumTestObject.get().getWebDriver();
  }

  /**
   * Get SeleniumWait.
   * 
   * @return SeleniumWait
   */
  public SeleniumWait getSeleniumWait() {
    return this.seleniumTestObject.get().getSeleniumWait();
  }

  /**
   * Get the seleniumTestObject for this test.
   * 
   * @return The seleniumTestObject
   */
  public SeleniumTestObject getSeleniumTestObject() {
    return this.seleniumTestObject.get();
  }

  /**
   * Log info about the web driver setup.
   */
  @Override
  protected void postSetupLogging() {
    try {

      if (SeleniumConfig.getBrowserName().equalsIgnoreCase("Remote")) {
        this.getLogger().logMessage(MessageType.INFORMATION, "Remote driver: %s",
            SeleniumConfig.getRemoteBrowserName());
      } else {
        this.getLogger().logMessage(MessageType.INFORMATION, "Loaded driver: %s",
            SeleniumConfig.getBrowserName());
      }

      WebDriver driver = SeleniumConfig.browser();
      SeleniumWait wait = new SeleniumWait(driver);

      seleniumTestObject.set(new SeleniumTestObject(driver, wait, this.getLogger(),
          this.getFullyQualifiedTestClassName()));      
    } catch (Exception e) {
      this.getLogger().logMessage(MessageType.ERROR, "Failed to start driver because: %s",
          e.getMessage());
      System.out.println(
          StringProcessor.safeFormatter("Browser type %s is not supported", e.getMessage()));
    }
  }

  /**
   * Take a screen shot if needed and tear down the web driver.
   * 
   * @param resultType
   *          The test result type
   */
  @Override
  protected void beforeLoggingTeardown(ITestResult resultType) {
    // Try to take a screen shot
    try {
      if (this.getWebDriver() != null && resultType.getStatus() != ITestResult.SUCCESS 
          && this.getLoggingEnabledSetting() != LoggingEnabled.NO) {

        captureScreenShot(this.getWebDriver(), this.getLogger(), ""); 
      }       
    } catch (Exception e) {
      this.tryToLog(MessageType.WARNING, "Failed to get screen shot because: %s", e.getMessage());
    }

    this.tryToLog(MessageType.INFORMATION, "Close");

    // tear down the web driver
    try {
      this.seleniumTestObject.get().webDriver.quit();
    } catch (Exception e) {
      this.tryToLog(MessageType.WARNING, "Failed to quit because: %s", e.getMessage());
    }
  }

  /**
   * Capture Screenshot.
   * @return Path to screenshot.
   */
  protected String captureScreenShot(WebDriver driver, Logger log, String fileName) {
    return SeleniumUtilities.captureScreenshot(driver, log, fileName);
  }
}
