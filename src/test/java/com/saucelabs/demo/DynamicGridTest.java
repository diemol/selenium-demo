package com.saucelabs.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

@Execution(ExecutionMode.CONCURRENT)
public class DynamicGridTest {

  private final String gridUrl =  "http://localhost:4444";

  @Test
  public void chromeTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL(this.gridUrl);
    RemoteWebDriver driver = new RemoteWebDriver(gridUrl, setOptions(new ChromeOptions()));
    try {
      navigate(driver);
    } finally {
      driver.quit();
    }
  }

  @Test
  public void firefoxTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL(this.gridUrl);
    RemoteWebDriver driver = new RemoteWebDriver(gridUrl, setOptions(new FirefoxOptions()));
    try {
      navigate(driver);
    } finally {
      driver.quit();
    }
  }

  @Test
  public void edgeTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL(this.gridUrl);
    RemoteWebDriver driver = new RemoteWebDriver(gridUrl, setOptions(new EdgeOptions()));
    try {
      navigate(driver);
    } finally {
      driver.quit();
    }
  }

  private void navigate(RemoteWebDriver driver) throws InterruptedException {
    driver.get("https://www.saucedemo.com");
    driver.findElement(By.tagName("body"));
    // For demo purposes
    Thread.sleep(5000);
  }

  private MutableCapabilities setOptions(MutableCapabilities options) {
    options.setCapability("se:recordVideo", true);
    options.setCapability("se:timeZone", "US/Pacific");
    options.setCapability("se:screenResolution", "1920x1080");
    options.setCapability("se:name", "Sample Test");
    options.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX);
    return options;
  }

}
