package com.saucelabs.demo;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

// Errors have now links to explain them better and fix suggestions
public class DocumentationLinksInErrorsTest {
	@Test
	public void wrongLocator() {
		ChromeDriver driver = new ChromeDriver();
		try {
			driver.get("https:/selenium.dev");
			driver.findElement(By.id("non-existing-id"));
		} finally {
			driver.quit();
		}
	}



}
