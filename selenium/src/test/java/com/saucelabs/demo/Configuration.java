package com.saucelabs.demo;

public class Configuration {

  // GitHub repo and then open in GitPod https://github.com/diemol/my-visual-todo-app
  public static String MY_TODO_APP_URL = "https://visual-todo-app-84499c59b74b.herokuapp.com/";

  public static void sleepTight(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
