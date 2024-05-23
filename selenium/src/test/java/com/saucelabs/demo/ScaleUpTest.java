package com.saucelabs.demo;

import static java.util.concurrent.TimeUnit.MINUTES;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class ScaleUpTest {

  private final ExecutorService executor =
    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 9);

  @Test
  public void chromeTest()
    throws MalformedURLException, InterruptedException, ExecutionException, TimeoutException {
    URL gridUrl = new URL("http://localhost:4444");
    int nTests = 100;
    ClientConfig config = ClientConfig.defaultConfig()
                            .connectionTimeout(Duration.ofMinutes(30))
                            .readTimeout(Duration.ofMinutes(30));
    CompletableFuture<?>[] futures = new CompletableFuture<?>[nTests];
    for (int i = 0; i < futures.length; i++) {
      CompletableFuture<Object> future = new CompletableFuture<>();
      futures[i] = future;
      Capabilities capabilities = i % 2 == 0 ? new ChromeOptions().addArguments("--headless") : new FirefoxOptions().addArguments("-headless");
      executor.submit(() -> {
        try {
          WebDriver driver = RemoteWebDriver.builder()
            .oneOf(capabilities)
            .address(gridUrl)
            .config(config)
            .build();
          long sleepLength = 20000;
          driver.get("https://www.selenium.dev/");
          // For demo purposes
          Thread.sleep(sleepLength);
          driver.get("https://opensource.saucelabs.com/");
          // For demo purposes
          Thread.sleep(sleepLength);
          // And now quit
          driver.quit();
          future.complete(true);
        } catch (Exception e) {
          future.completeExceptionally(e);
        }
      });
    }
    CompletableFuture.allOf(futures).get(60, MINUTES);
  }

}
