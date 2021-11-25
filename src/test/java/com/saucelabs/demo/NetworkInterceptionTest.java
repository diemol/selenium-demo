package com.saucelabs.demo;

import static com.saucelabs.demo.Configuration.MY_TODO_APP_URL;
import static com.saucelabs.demo.Configuration.sleepTight;
import static org.openqa.selenium.remote.http.HttpMethod.DELETE;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import static com.google.common.net.MediaType.JPEG;

import com.google.common.collect.ImmutableMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.remote.http.Contents;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.Routable;
import org.openqa.selenium.remote.http.Route;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class NetworkInterceptionTest {

  private static URL APP_URL;

  static {
    try {
      APP_URL = new URL(MY_TODO_APP_URL);
    } catch (MalformedURLException ignore) {
      // fall off
    }
  }

  private WebDriver driver;
  private Wait<WebDriver> wait;

  @BeforeEach
  void createSetup() {
    ClientConfig clientConfig = ClientConfig
      .defaultConfig()
      .authenticateAs(new UsernameAndPassword("admin", "admin"))
      .baseUrl(APP_URL);
    HttpClient client = HttpClient.Factory.createDefault().createClient(clientConfig);
    HttpResponse response = client.execute(new HttpRequest(DELETE, APP_URL.toString() + "/items"));
    Assertions.assertEquals(200, response.getStatus());
    driver = new ChromeDriver();
    ((HasAuthentication) driver).register(UsernameAndPassword.of("admin", "admin"));
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  @AfterEach
  void quitDriver() {
    driver.quit();
  }

  @Test
  public void addItem() {
    String item = "Comprar pan";
    driver.get(APP_URL.toString());

    String inputFieldLocator = "input[data-testid='new-item-text']";
    WebElement inputField = wait.until(presenceOfElementLocated(By.cssSelector(inputFieldLocator)));
    inputField.sendKeys(item);

    driver.findElement(By.cssSelector("button[data-testid='new-item-button']")).click();

    String itemLocator = String.format("div[data-testid='%s']", item);
    List<WebElement> addedItem = wait.until(presenceOfAllElementsLocatedBy(By.cssSelector(itemLocator)));

    Assertions.assertEquals(1, addedItem.size());

    sleepTight(3000);
  }

  @Test
  void addItemReplacingImage() throws IOException {
    String item = "Comprar arroz";
    Path path = Paths.get("src/test/resources/sl-holidays-bot-450x200.png");
    byte[] sauceBotImage = Files.readAllBytes(path);
    Routable replaceImage = Route
      .matching(req -> req.getUri().contains("unsplash.com"))
      .to(() -> req -> new HttpResponse()
        .addHeader("Content-Type", JPEG.toString())
        .setContent(Contents.bytes(sauceBotImage)));

    try (NetworkInterceptor ignore = new NetworkInterceptor(driver, replaceImage)) {
      driver.get(APP_URL.toString());

      String inputFieldLocator = "input[data-testid='new-item-text']";
      WebElement inputField = wait.until(presenceOfElementLocated(By.cssSelector(inputFieldLocator)));
      inputField.sendKeys(item);

      driver.findElement(By.cssSelector("button[data-testid='new-item-button']")).click();

      String itemLocator = String.format("div[data-testid='%s']", item);
      List<WebElement> addedItem = wait.until(presenceOfAllElementsLocatedBy(By.cssSelector(itemLocator)));

      Assertions.assertEquals(1, addedItem.size());
    }

    sleepTight(4000);
  }

  @Test
  void addItemReplacingResponse() {
    String item = "Limpiar los baños";
    String mockedItem = "Ir al parque";

    Routable apiPost = Route
      .matching(req -> req.getUri().contains("items") && req.getMethod().equals(HttpMethod.POST))
      .to(() -> req -> new HttpResponse()
        .addHeader("Content-Type", "application/json; charset=utf-8")
        .setStatus(200)
        .setContent(
          Contents.asJson(
            ImmutableMap.of("id", "f2a5514c-f451-43a6-825c-8753a2566d6e",
                            "name", mockedItem,
                            "completed", false))));

    try (NetworkInterceptor ignore = new NetworkInterceptor(driver, apiPost)) {
      driver.get(APP_URL.toString());

      String inputFieldLocator = "input[data-testid='new-item-text']";
      WebElement inputField = wait.until(presenceOfElementLocated(By.cssSelector(inputFieldLocator)));
      inputField.sendKeys(item);

      driver.findElement(By.cssSelector("button[data-testid='new-item-button']")).click();

      String itemLocator = String.format("div[data-testid='%s']", mockedItem);
      List<WebElement> addedItem = wait.until(presenceOfAllElementsLocatedBy(By.cssSelector(itemLocator)));

      Assertions.assertEquals(1, addedItem.size());
    }

    sleepTight(5000);
  }
}
