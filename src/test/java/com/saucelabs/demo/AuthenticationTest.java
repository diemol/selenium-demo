package com.saucelabs.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AuthenticationTest {

  private WebDriver driver;

  @BeforeEach
  void createDriver() {
    driver = new ChromeDriver();
  }

  @AfterEach
  void quitDriver() {
    driver.quit();
  }

  @Test
  public void authenticate() {
    ((HasAuthentication) driver).register(UsernameAndPassword.of("admin", "admin"));

    driver.get("http://localhost:3000");

    Assertions.assertEquals("My visual ToDo App", driver.getTitle());
  }

}
