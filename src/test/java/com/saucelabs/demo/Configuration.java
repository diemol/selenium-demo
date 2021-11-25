package com.saucelabs.demo;

public class Configuration {

  public static String MY_TODO_APP_URL = "https://3000-salmon-ladybug-pimmgsfx.ws-eu17.gitpod.io";

  public static void sleepTight(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
