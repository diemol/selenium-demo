package com.saucelabs.demo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.MediaType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.devtools.v96.network.Network;
import org.openqa.selenium.devtools.v96.network.model.BlockedReason;
import org.openqa.selenium.devtools.v96.network.model.Headers;
import org.openqa.selenium.devtools.v96.network.model.ResourceType;
import org.openqa.selenium.remote.http.Contents;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.Route;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class BiDiTest {
	private ChromeDriver chromeDriver;
	private DevTools devTools;

	@BeforeEach
	public void setUp() {
		chromeDriver = new ChromeDriver();
		devTools = chromeDriver.getDevTools();
		devTools.createSession();
	}

	@Test
	public void blockUrls() throws InterruptedException {
		// Network enabled
		devTools.send(new Command<>("Network.enable", ImmutableMap.of()));

		// Block urls that have png and css
		devTools.send(Network.setBlockedURLs(ImmutableList.of("*.css", "*.png")));

    // Listening to events and check that the urls are actually blocked
		devTools.addListener(Network.loadingFailed(), loadingFailed -> {
			if (loadingFailed.getType().equals(ResourceType.STYLESHEET) ||
          loadingFailed.getType().equals(ResourceType.IMAGE)) {
        BlockedReason blockedReason = loadingFailed.getBlockedReason().orElse(null);
        Assertions.assertEquals(blockedReason, BlockedReason.INSPECTOR);
			}
		});

		chromeDriver.get("https://www.diemol.com/selenium-4-demo/relative-locators-demo.html");
		// Thread.sleep only meant for demo purposes!
		Thread.sleep(5000);

		// Disabling network interception and reloading the site
		devTools.send(Network.disable());
		chromeDriver.get("https://www.diemol.com/selenium-4-demo/relative-locators-demo.html");
		// Thread.sleep only meant for demo purposes!
		Thread.sleep(5000);
	}

	@Test
	public void addExtraHeaders() throws InterruptedException {
		chromeDriver.get("https://manytools.org/http-html-text/http-request-headers/");
		// Thread.sleep only meant for demo purposes!
		Thread.sleep(10000);

    devTools.send(new Command<>("Network.enable", ImmutableMap.of()));

		devTools.send(Network.setExtraHTTPHeaders(
			new Headers(ImmutableMap.of("meetup", "STUGRM"))));

		devTools.addListener(Network.loadingFailed(), loadingFailed -> {
			if (loadingFailed.getType().equals(ResourceType.STYLESHEET)) {
        BlockedReason blockedReason = loadingFailed.getBlockedReason().orElse(null);
				Assertions.assertEquals(blockedReason, BlockedReason.INSPECTOR);
			}
		});

		devTools.addListener(Network.requestWillBeSent(),
				requestWillBeSent ->
						Assertions.assertEquals(
							requestWillBeSent.getRequest().getHeaders().get("meetup"), "STUGRM"));

		devTools.addListener(Network.dataReceived(),
				dataReceived -> Assertions.assertNotNull(dataReceived.getRequestId()));

		chromeDriver.get("https://manytools.org/http-html-text/http-request-headers/");
		// Thread.sleep only meant for demo purposes!
		Thread.sleep(10000);
	}

	@Test
	public void getPageScreenshot() throws IOException {
		chromeDriver.get("https://opensource.saucelabs.com/");
		Map<String, Object> result =
			chromeDriver.executeCdpCommand("Page.captureScreenshot", new HashMap<>());
		String data = (String) result.get("data");
		byte[] image = Base64.getDecoder().decode((data));
		Files.write(new File("screenshotFileName.png").toPath(), image);
	}

	@Test
	public void getFullPageScreenshot() throws IOException {
		chromeDriver.get("https://opensource.saucelabs.com/");
		long width = (long) chromeDriver.executeScript("return document.body.scrollWidth");
		long height = (long) chromeDriver.executeScript("return document.body.scrollHeight");
		long scale = (long) chromeDriver.executeScript("return window.devicePixelRatio");

		HashMap<String, Object> setDeviceMetricsOverride = new HashMap<>();
		setDeviceMetricsOverride.put("deviceScaleFactor", scale);
		setDeviceMetricsOverride.put("mobile", false);
		setDeviceMetricsOverride.put("width", width);
		setDeviceMetricsOverride.put("height", height);
		chromeDriver.executeCdpCommand(
			"Emulation.setDeviceMetricsOverride", setDeviceMetricsOverride);

		Map<String, Object> result =
			chromeDriver.executeCdpCommand("Page.captureScreenshot", new HashMap<>());
		String data = (String) result.get("data");
		byte[] image = Base64.getDecoder().decode((data));
		Files.write(new File("fullPageScreenshotFileName.png").toPath(), image);
	}

	@Test
	public void emulateTimezoneTest() throws InterruptedException {
		Map<String, Object> timezoneInfo = new HashMap<>();
		timezoneInfo.put("timezoneId", "America/Montevideo");

		devTools.send(new Command<>("Network.enable", ImmutableMap.of()));
		chromeDriver.executeCdpCommand("Emulation.setTimezoneOverride", timezoneInfo);
		chromeDriver.get("https://everytimezone.com/");

		// Thread.sleep only meant for demo purposes!
		Thread.sleep(10000);
	}

	@AfterEach
	public void tearDown() {
		chromeDriver.quit();
	}
}
