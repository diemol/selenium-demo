package com.saucelabs.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;

@Execution(ExecutionMode.CONCURRENT)
public class GridTest {

  @Test
  public void chromeTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL("http://localhost:4444");
    RemoteWebDriver webDriver = new RemoteWebDriver(gridUrl, new ChromeOptions());
    try {
      navigate(webDriver);
    } finally {
      webDriver.quit();
    }
  }

  @Test
  public void firefoxTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL("http://localhost:4444");
    RemoteWebDriver webDriver = new RemoteWebDriver(gridUrl, new FirefoxOptions());
    try {
      navigate(webDriver);
    } finally {
      webDriver.quit();
    }
  }

  @Test
  public void edgeTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL("http://localhost:4444");
    RemoteWebDriver webDriver = new RemoteWebDriver(gridUrl, new EdgeOptions());
    try {
      navigate(webDriver);
    } finally {
      webDriver.quit();
    }
  }

  @Test
  public void safariTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL("http://localhost:4444");
    RemoteWebDriver webDriver = new RemoteWebDriver(gridUrl, new SafariOptions());
    try {
      navigate(webDriver);
    } finally {
      webDriver.quit();
    }
  }

  private void navigate(RemoteWebDriver webDriver) throws InterruptedException {
    long sleepLength = 10000;
    webDriver.manage().window().maximize();
    webDriver.get("https://www.selenium.dev/");
    Thread.sleep(sleepLength);
    webDriver.get("https://opensource.saucelabs.com/");
    Thread.sleep(sleepLength);
  }

}

/*
# Tracing
docker run --rm -it --name jaeger \
  -p 16686:16686 \
  -p 14250:14250 \
  jaegertracing/all-in-one:1.19.2

  http://localhost:16686/

java -Dotel.traces.exporter=jaeger \
     -Dotel.exporter.jaeger.endpoint=localhost:14250 \
     -Dotel.resource.attributes=service.name=selenium-standalone \
     -jar selenium-server-4.0.0-prerelease-rc-1-c498dad8c5.jar \
     --ext $(coursier fetch -p \
        io.opentelemetry:opentelemetry-exporter-jaeger:1.0.0 \
        io.grpc:grpc-netty:1.35.0) \
     standalone
 */
