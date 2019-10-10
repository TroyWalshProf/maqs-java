/*
 * Copyright 2019 (C) Magenic, All rights Reserved
 */

package com.magenic.jmaqs.appium;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppiumDriverFactoryTest {

  private static final String MESSAGE = "Test method not implemented yet.";

  @Test
  public void testGetDefaultMobileDriver() {
    AppiumDriver<WebElement> defaultMobileDriver = AppiumDriverFactory.getDefaultMobileDriver();
    Assert.assertNotNull(defaultMobileDriver, "Checking if default driver is null");
  }

  @Test
  public void testTestGetDefaultMobileDriver() {

    throw new UnsupportedOperationException(MESSAGE);
  }

  @Test
  public void testGetDefaultMobileOptions() {
    throw new UnsupportedOperationException(MESSAGE);
  }

  @Test
  public void testTestGetDefaultMobileOptions() {
    throw new UnsupportedOperationException(MESSAGE);
  }

  @Test
  public void testGetAndroidDriver() {
    throw new UnsupportedOperationException(MESSAGE);
  }

  @Test
  public void testGetIOSDriver() {
    throw new UnsupportedOperationException(MESSAGE);
  }

  @Test
  public void testGetWindowsDriver() {
    throw new UnsupportedOperationException(MESSAGE);
  }

  @Test
  public void testCreateDriver() {
    throw new UnsupportedOperationException(MESSAGE);
  }
}