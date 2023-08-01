package com.saucelabs.demo;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class SeleniumManagerTest {
	@Test
	public void browserBinaryFirefoxDevEdition() throws InterruptedException {
		FirefoxOptions options = new FirefoxOptions();
		options.setBinary("/Applications/Firefox Developer Edition.app/Contents/MacOS/firefox-bin");
		FirefoxDriver driver = new FirefoxDriver(options);
		driver.get("https:/selenium.dev");
		// Thread.sleep only meant for demo purposes!
		Thread.sleep(5000);
		driver.quit();
	}

	@Test
	public void browserBinaryChromeBeta() throws InterruptedException {
		ChromeOptions options = new ChromeOptions();
		options.setBinary("/Applications/Google Chrome Beta.app/Contents/MacOS/Google Chrome Beta");
		ChromeDriver driver = new ChromeDriver(options);
		driver.get("https:/selenium.dev");
		// Thread.sleep only meant for demo purposes!
		Thread.sleep(5000);
		driver.quit();
	}



}
