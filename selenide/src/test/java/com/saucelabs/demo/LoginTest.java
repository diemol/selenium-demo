package com.saucelabs.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.junit5.TextReportExtension;

@ExtendWith({TextReportExtension.class})
public class LoginTest {

  @Test
  public void userCanLogin() {
    open("https://the-internet.herokuapp.com/login");
    $("#username").setValue("tomsmith");
    $("#password").setValue("SuperSecretPassword!").pressEnter();
    $("#flash").shouldHave(text("You logged into a secure area!"));
  }

  // User logs in to Sauce Demo and verifies the Products page
  @Test
  public void userCanLoginToSauceDemo() {
    open("https://www.saucedemo.com/");
    $("#user-name").setValue("standard_user");
    $("#password").setValue("secret_sauce").pressEnter();
    $(by("data-test","title")).shouldHave(text("Products"));
  }
}
