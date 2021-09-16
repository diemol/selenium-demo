package com.saucelabs.demo;

import static java.util.concurrent.TimeUnit.MINUTES;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class DynamicGridTest {

  private final int nTests = 2;
  private final String gridUrl =  "http://localhost:4444";
  private final ExecutorService executor =
    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 5);

  @Test
  public void chromeTest()
    throws MalformedURLException, InterruptedException, ExecutionException, TimeoutException {
    URL gridUrl = new URL(this.gridUrl);
    CompletableFuture<?>[] futures = new CompletableFuture<?>[nTests];
    for (int i = 0; i < futures.length; i++) {
      CompletableFuture<Object> future = new CompletableFuture<>();
      futures[i] = future;
      executor.submit(() -> {
        try {
          WebDriver driver = new RemoteWebDriver(gridUrl, setOptions(new ChromeOptions()));
          driver.get("https://www.saucedemo.com");
          driver.findElement(By.tagName("body"));
          // For demo purposes
          Thread.sleep(5000);
          // And now quit
          driver.quit();
          future.complete(true);
        } catch (Exception e) {
          future.completeExceptionally(e);
        }
      });
    }
    CompletableFuture.allOf(futures).get(6, MINUTES);
  }

  @Test
  public void firefoxTest()
    throws MalformedURLException, InterruptedException, ExecutionException, TimeoutException {
    URL gridUrl = new URL(this.gridUrl);
    CompletableFuture<?>[] futures = new CompletableFuture<?>[nTests];
    for (int i = 0; i < futures.length; i++) {
      CompletableFuture<Object> future = new CompletableFuture<>();
      futures[i] = future;
      executor.submit(() -> {
        try {
          WebDriver driver = new RemoteWebDriver(gridUrl, setOptions(new FirefoxOptions()));
          driver.get("https://www.saucedemo.com");
          driver.findElement(By.tagName("body"));
          // For demo purposes
          Thread.sleep(5000);
          // And now quit
          driver.quit();
          future.complete(true);
        } catch (Exception e) {
          future.completeExceptionally(e);
        }
      });
    }
    CompletableFuture.allOf(futures).get(4, MINUTES);
  }

  @Test
  public void edgeTest()
    throws MalformedURLException, InterruptedException, ExecutionException, TimeoutException {
    URL gridUrl = new URL(this.gridUrl);
    CompletableFuture<?>[] futures = new CompletableFuture<?>[nTests];
    for (int i = 0; i < futures.length; i++) {
      CompletableFuture<Object> future = new CompletableFuture<>();
      futures[i] = future;
      executor.submit(() -> {
        try {
          WebDriver driver = new RemoteWebDriver(gridUrl, setOptions(new EdgeOptions()));
          driver.get("https://www.saucedemo.com");
          driver.findElement(By.tagName("body"));
          // For demo purposes
          Thread.sleep(5000);
          // And now quit
          driver.quit();
          future.complete(true);
        } catch (Exception e) {
          future.completeExceptionally(e);
        }
      });
    }
    CompletableFuture.allOf(futures).get(4, MINUTES);
  }

  private MutableCapabilities setOptions(MutableCapabilities options) {
    options.setCapability("se:recordVideo", true);
    options.setCapability("se:timeZone", "US/Pacific");
    options.setCapability("se:screenResolution", "1920x1080");
    options.setCapability("se:name", "Sample Test");
    return options;
  }

}
