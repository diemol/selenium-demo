package com.saucelabs.demo;

import static com.saucelabs.demo.Configuration.MY_TODO_APP_URL;
import static com.saucelabs.demo.Configuration.sleepTight;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ShadowDomTest {

  private WebDriver driver;

  @BeforeEach
  void createDriver() {
    driver = new ChromeDriver();
    ((HasAuthentication) driver).register(UsernameAndPassword.of("admin", "admin"));
  }

  @AfterEach
  void quitDriver() {
    driver.quit();
  }

  @Test
  public void checkAppNameInShadowDom() {
    driver.get(MY_TODO_APP_URL);

    WebElement appName = driver.findElement(By.tagName("app-name"));

    SearchContext shadowRoot = appName.getShadowRoot();

    WebElement appNameInsideShadowDom = shadowRoot.findElement(By.id("app-name"));

    // Sleep only meant for demo purposes!
    sleepTight(3000);

    Assertions.assertEquals("My visual ToDo App", appNameInsideShadowDom.getText());
  }

}
