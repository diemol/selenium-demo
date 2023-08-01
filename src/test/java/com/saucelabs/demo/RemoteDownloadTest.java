package com.saucelabs.demo;

import com.google.common.collect.ImmutableMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.Zip;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.remote.http.Contents.asJson;
import static org.openqa.selenium.remote.http.Contents.string;
import static org.openqa.selenium.remote.http.HttpMethod.GET;
import static org.openqa.selenium.remote.http.HttpMethod.POST;

public class RemoteDownloadTest {

	String remoteUrl = "https://4444-seleniumhq-dockerseleni-tyq9728bbxo.ws-eu97.gitpod.io";

	@Test
	void canListDownloadedFiles() throws InterruptedException, MalformedURLException {
		URL gridUrl = new URL(remoteUrl);
		ChromeOptions options = new ChromeOptions();
		options.setCapability("se:downloadsEnabled", true);
		RemoteWebDriver driver = new RemoteWebDriver(gridUrl, options);
		driver.get("https://www.selenium.dev/selenium/web/downloads/download.html");
		driver.findElement(By.id("file-1")).click();
		driver.findElement(By.id("file-2")).click();

		// Waiting for the file to be remotely downloaded
		TimeUnit.SECONDS.sleep(3);

		//This is the endpoint which will provide us with list of files to download and also to
		//let us download a specific file.
		String downloadsEndpoint = String.format("/session/%s/se/files", driver.getSessionId());

		try (HttpClient client = HttpClient.Factory.createDefault().createClient(gridUrl)) {
			HttpRequest request = new HttpRequest(GET, downloadsEndpoint);
			HttpResponse response = client.execute(request);
			Map<String, Object> jsonResponse = new Json().toType(string(response), Json.MAP_TYPE);
			@SuppressWarnings("unchecked")
			Map<String, Object> value = (Map<String, Object>) jsonResponse.get("value");
			@SuppressWarnings("unchecked")
			List<String> names = (List<String>) value.get("names");
			assertTrue(names.contains("file_1.txt"));
			assertTrue(names.contains("file_2.jpg"));
		} finally {
			driver.quit();
		}
	}

	@Test
	void testCanDownloadFiles() throws InterruptedException, IOException {
		URL gridUrl = new URL(remoteUrl);
		RemoteWebDriver driver = new RemoteWebDriver(gridUrl, new ChromeOptions());
		driver.get("https://www.selenium.dev/selenium/web/downloads/download.html");
		driver.findElement(By.id("file-1")).click();

		// Waiting for the file to be remotely downloaded
		TimeUnit.SECONDS.sleep(3);

		//This is the endpoint which will provide us with list of files to download and also to
		//let us download a specific file.
		String downloadsEndpoint = String.format("/session/%s/se/files", driver.getSessionId());

		try (HttpClient client = HttpClient.Factory.createDefault().createClient(gridUrl)) {
			HttpRequest request = new HttpRequest(POST, downloadsEndpoint);
			request.setContent(asJson(ImmutableMap.of("name", "file_1.txt")));
			HttpResponse response = client.execute(request);
			Map<String, Object> jsonResponse = new Json().toType(string(response), Json.MAP_TYPE);
			@SuppressWarnings("unchecked")
			Map<String, Object> value = (Map<String, Object>) jsonResponse.get("value");
			String zippedContents = value.get("contents").toString();
			File downloadDir = Zip.unzipToTempDir(zippedContents, "download", "");
			File downloadedFile = Optional.ofNullable(downloadDir.listFiles()).orElse(new File[]{})[0];
			String fileContent = String.join("", Files.readAllLines(downloadedFile.toPath()));
			Assertions.assertEquals("Hello, World!", fileContent);
		} finally {
			driver.quit();
		}
	}

}
