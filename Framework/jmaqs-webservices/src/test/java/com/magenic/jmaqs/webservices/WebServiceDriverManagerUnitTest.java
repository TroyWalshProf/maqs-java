/*
 * Copyright 2019 (C) Magenic, All rights Reserved
 */

package com.magenic.jmaqs.webservices;

import com.magenic.jmaqs.base.BaseTest;
import com.magenic.jmaqs.utilities.helper.TestCategories;
import java.net.URISyntaxException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.Test;

/**
 * Unit Tests for Web Service Driver Manager.
 */
public class WebServiceDriverManagerUnitTest extends BaseTest {
  /**
   * Test for Getting Web Service Driver using Supplier in constructor.
   *
   * @throws URISyntaxException URI Syntax Exception
   */
  @Test(groups = TestCategories.WebService)
  public void getWebServiceDriverWithSupplierTest() throws URISyntaxException {
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    webServiceDriver.setHttpClient(webServiceDriver.getHttpClient(MediaType.APP_JSON.getMediaTypeString()));

    WebServiceDriverManager driverManager = new WebServiceDriverManager(
        () -> webServiceDriver.getHttpClient(MediaType.APP_JSON.getMediaTypeString()),
        this.getTestObject());

    Assert.assertNotNull(driverManager.getWebServiceDriver(), "Expected Web Service Driver to not be null");
  }

  /**
   * Test for Getting Web Service Driver using Web Service Driver in constructor.
   *
   * @throws URISyntaxException URI Syntax Exception
   */
  @Test(groups = TestCategories.WebService)
  public void getWebServiceDriverTest() throws URISyntaxException {
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    WebServiceDriverManager driverManager = new WebServiceDriverManager(webServiceDriver, this.getTestObject());

    Assert.assertNotNull(driverManager.getWebServiceDriver(), "Expected Web Service Driver to not be null");
  }

  /**
   * Test for Getting Web Service Driver when Driver is null and instantiates default Driver.
   *
   * @throws URISyntaxException URI Syntax Exception
   */
  @Test(groups = TestCategories.WebService)
  public void getWebServiceDriverNullDriver() throws URISyntaxException {
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    WebServiceDriverManager driverManager = new WebServiceDriverManager(webServiceDriver, this.getTestObject());

    // Set the Driver to be null then check Get Web Service Driver creates default Driver.
    driverManager.overrideDriver(null);
    Assert.assertNotNull(driverManager.getWebServiceDriver(), "Expected Default Web Service Driver to be created.");
  }

  /**
   * Test for Overriding Web Service Driver.
   *
   * @throws URISyntaxException URI Syntax Exception
   */
  @Test(groups = TestCategories.WebService)
  public void overrideWebServiceDriverTest() throws URISyntaxException {
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    WebServiceDriver webServiceDriver2 = new WebServiceDriver("http://www.google.com/");

    WebServiceDriverManager driverManager = new WebServiceDriverManager(webServiceDriver, this.getTestObject());
    driverManager.overrideDriver(webServiceDriver2);

    Assert.assertEquals(driverManager.getWebServiceDriver().getBaseWebServiceAddress().toString(), "http://www.google.com/");
  }

  /**
   * Test for Closing Web Service Driver.
   *
   * @throws URISyntaxException URI Syntax Exception
   */
  @Test(groups = TestCategories.WebService)
  public void closeWebServiceDriverTest() throws URISyntaxException {
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    WebServiceDriverManager driverManager = new WebServiceDriverManager(webServiceDriver, this.getTestObject());

    driverManager.close();
    Assert.assertNull(driverManager.getBaseDriver(), "Expected Base Driver to be null.");
  }

  @Override protected void postSetupLogging() throws Exception {

  }

  @Override protected void beforeLoggingTeardown(ITestResult resultType) {

  }
}
