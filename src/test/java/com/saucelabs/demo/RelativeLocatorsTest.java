package com.saucelabs.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.remote.http.HttpMethod.DELETE;
import static org.openqa.selenium.support.locators.RelativeLocator.with;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class RelativeLocatorsTest {

	private static URL appUrl;

	static {
		try {
			appUrl = new URL("http://localhost:3000");
		} catch (MalformedURLException ignore) {
			// fall off
		}
	}
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeEach
	void createDriver() {
		ClientConfig clientConfig = ClientConfig
			.defaultConfig()
			.authenticateAs(new UsernameAndPassword("admin", "admin"))
			.baseUrl(appUrl);
		HttpClient client = HttpClient.Factory.createDefault().createClient(clientConfig);
		client.execute(new HttpRequest(DELETE, appUrl.toString() + "/items"));
		driver = new ChromeDriver();
		((HasAuthentication) driver).register(UsernameAndPassword.of("admin", "admin"));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@Test
	public void relativeLocators() {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		driver.manage().window().maximize();
		driver.get("https://www.diemol.com/selenium-4-demo/relative-locators-demo.html");

		sleepTight(5000);

		WebElement element = driver.findElement(with(By.tagName("li"))
				.toLeftOf(By.id("boston"))
				.below(By.id("warsaw")));
		blur(jsExecutor, element);
		unblur(jsExecutor, element);

		driver.quit();
	}

	@Test
	public void locatingItemsByTheirRelativeLocation() {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		String topItem = "Comprar queso";
		String middleItem = "Comprar vino";
		String bottomItem = "Limpiar la mesa";

		driver.manage().window().maximize();
		driver.get("http://localhost:3000");

		addItem(driver, bottomItem);
		addItem(driver, middleItem);
		addItem(driver, topItem);

		driver.navigate().refresh();

		String itemLocator = String.format("div[data-testid='%s']", middleItem);
		WebElement above = driver.findElement(with(By.className("name"))
																						.above(By.cssSelector(itemLocator)));
		blur(jsExecutor, above);
		unblur(jsExecutor, above);
		Assertions.assertEquals(topItem, above.getText());

		WebElement below = driver.findElement(with(By.className("name"))
																						.below(By.cssSelector(itemLocator)));
		blur(jsExecutor, below);
		unblur(jsExecutor, below);
		Assertions.assertEquals(bottomItem, below.getText());

		driver.quit();
	}

	void addItem(WebDriver driver, String item) {
		String inputFieldLocator = "input[data-testid='new-item-text']";
		WebElement inputField = wait.until(presenceOfElementLocated(By.cssSelector(inputFieldLocator)));
		inputField.sendKeys(item);

		driver.findElement(By.cssSelector("button[data-testid='new-item-button']")).click();

		String itemLocator = String.format("div[data-testid='%s']", item);
		List<WebElement> addedItem = wait.until(presenceOfAllElementsLocatedBy(By.cssSelector(itemLocator)));

		Assertions.assertEquals(1, addedItem.size());
	}

	public void blur(JavascriptExecutor jsExecutor, WebElement webElement) {
		jsExecutor.executeScript("arguments[0].style.filter='blur(8px)'", webElement);
		// Thread.sleep only meant for demo purposes!
		sleepTight(5000);
	}

	public void unblur(JavascriptExecutor jsExecutor, WebElement webElement) {
		jsExecutor.executeScript("arguments[0].style.filter='blur(0px)'", webElement);
	}

	private static void sleepTight(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}
}
