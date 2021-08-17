package com.saucelabs.demo;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DynamicGridTest {

  @Test
  public void chromeTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL("http://localhost:4444");
    ChromeOptions options = new ChromeOptions();
    options.setCapability("se:recordVideo", true);
    options.setCapability("se:timeZone", "US/Pacific");
    options.setCapability("se:screenResolution", "1920x1080");
    RemoteWebDriver webDriver = new RemoteWebDriver(gridUrl, options);
    try {
      navigate(webDriver);
    } finally {
      webDriver.quit();
    }
  }

  @Test
  public void firefoxTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL("http://localhost:4444");
    FirefoxOptions options = new FirefoxOptions();
    options.setCapability("se:recordVideo", true);
    options.setCapability("se:timeZone", "US/Pacific");
    options.setCapability("se:screenResolution", "1920x1080");
    RemoteWebDriver webDriver = new RemoteWebDriver(gridUrl, options);
    try {
      navigate(webDriver);
    } finally {
      webDriver.quit();
    }
  }

  @Test
  public void edgeTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL("http://localhost:4444");
    EdgeOptions options = new EdgeOptions();
    options.setCapability("se:recordVideo", true);
    options.setCapability("se:timeZone", "US/Pacific");
    options.setCapability("se:screenResolution", "1920x1080");
    RemoteWebDriver webDriver = new RemoteWebDriver(gridUrl, options);
    try {
      navigate(webDriver);
    } finally {
      webDriver.quit();
    }
  }

  private void navigate(RemoteWebDriver webDriver) throws InterruptedException {
    long sleepLength = 2000;
    webDriver.manage().window().maximize();
    webDriver.get("https://www.selenium.dev/");
    Thread.sleep(sleepLength);
    webDriver.get("https://opensource.saucelabs.com/");
    Thread.sleep(sleepLength);
  }

}
