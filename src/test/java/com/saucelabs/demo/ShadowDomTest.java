package com.saucelabs.demo;

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
    driver.get("http://localhost:3000");

    WebElement appName = driver.findElement(By.tagName("app-name"));

    SearchContext shadowRoot = appName.getShadowRoot();

    WebElement appNameInsideShadowDom = shadowRoot.findElement(By.id("app-name"));

    Assertions.assertEquals("My visual ToDo App", appNameInsideShadowDom.getText());
  }

}
