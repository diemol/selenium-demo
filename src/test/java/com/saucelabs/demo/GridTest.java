package com.saucelabs.demo;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class GridTest {

/*
# Start socat
socat -4 TCP-LISTEN:2375,fork UNIX-CONNECT:/var/run/docker.sock
# Start the Grid (on the src/test/resources path)
docker run --rm -ti --name selenium-docker -p 4444:4444 \
    -v ${PWD}/config.toml:/opt/bin/config.toml \
    -v ${PWD}/assets:/opt/selenium/assets \
    selenium/standalone-docker:4.0.0-beta-4-20210608

# Tracing
java -DJAEGER_SERVICE_NAME="selenium-standalone" \
     -DJAEGER_AGENT_HOST=localhost \
     -DJAEGER_AGENT_PORT=14250 \
     -jar selenium-server-4.0.0-prerelease-beta-1-02d5e641d5.jar \
     --ext $(coursier fetch -p \
        io.opentelemetry:opentelemetry-exporter-jaeger:0.13.1 \
        io.grpc:grpc-netty:1.33.1) \
      standalone

docker run --rm -it --name jaeger \
  -p 16686:16686 \
  -p 14250:14250 \
  jaegertracing/all-in-one:1.19.2

http://localhost:16686/
 */

  @Test
  public void chromeTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL("http://localhost:4444");
    ChromeOptions options = new ChromeOptions();
    options.setCapability("se:recordVideo", true);
    options.setCapability("se:timeZone", "US/Pacific");
    options.setCapability("se:screenResolution", "1920x1080");
    RemoteWebDriver webDriver = new RemoteWebDriver(gridUrl, options);
    try {
      navigate(webDriver);
    } finally {
      webDriver.quit();
    }
  }

  @Test
  public void firefoxTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL("http://localhost:4444");
    FirefoxOptions options = new FirefoxOptions();
    options.setCapability("se:recordVideo", true);
    options.setCapability("se:timeZone", "US/Pacific");
    options.setCapability("se:screenResolution", "1920x1080");
    RemoteWebDriver webDriver = new RemoteWebDriver(gridUrl, options);
    try {
      navigate(webDriver);
    } finally {
      webDriver.quit();
    }
  }

  @Test
  public void edgeTest() throws MalformedURLException, InterruptedException {
    URL gridUrl = new URL("http://localhost:4444");
    EdgeOptions options = new EdgeOptions();
    options.setCapability("se:recordVideo", true);
    options.setCapability("se:timeZone", "US/Pacific");
    options.setCapability("se:screenResolution", "1920x1080");
    RemoteWebDriver webDriver = new RemoteWebDriver(gridUrl, options);
    try {
      navigate(webDriver);
    } finally {
      webDriver.quit();
    }
  }

  private void navigate(RemoteWebDriver webDriver) throws InterruptedException {
    long sleepLength = 5000;
    webDriver.manage().window().maximize();
    webDriver.get("https://time.is/");
    Thread.sleep(sleepLength);
    webDriver.get("https://www.amazon.de/");
    Thread.sleep(sleepLength);
    webDriver.get("https://www.airbnb.com/");
    Thread.sleep(sleepLength);
    webDriver.get("https://www.saucelabs.com/");
    Thread.sleep(sleepLength);
    webDriver.get("https://opensource.saucelabs.com/");
    Thread.sleep(sleepLength);
  }

}
