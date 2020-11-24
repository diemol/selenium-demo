package com.saucelabs.demo;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;

import static org.openqa.selenium.support.locators.RelativeLocator.withTagName;

public class RelativeLocatorsTest {

	@Test
	public void relativeLocators() throws InterruptedException {
		WebDriver webDriver = new SafariDriver();
		JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;

		webDriver.manage().window().maximize();
		webDriver.get("https://www.diemol.com/selenium-4-demo/relative-locators-demo.html");
		// Thread.sleep only meant for demo purposes!
		Thread.sleep(5000);
		WebElement element = webDriver.findElement(withTagName("li")
				.toLeftOf(By.id("boston"))
				.below(By.id("warsaw")));
		blur(jsExecutor, element);
		unblur(jsExecutor, element);

		webDriver.quit();
	}

	public void blur(JavascriptExecutor jsExecutor, WebElement webElement) throws InterruptedException {
		jsExecutor.executeScript("arguments[0].style.filter='blur(8px)'", webElement);
		// Thread.sleep only meant for demo purposes!
		Thread.sleep(5000);
	}

	public void unblur(JavascriptExecutor jsExecutor, WebElement webElement) throws InterruptedException {
		jsExecutor.executeScript("arguments[0].style.filter='blur(0px)'", webElement);
		// Thread.sleep only meant for demo purposes!
		Thread.sleep(5000);
	}
}
