package com.jbroker.logger;

public class Logger {

  private static final Logger LOGGER = new Logger();

  private Logger() {
  }

  public static Logger getInstance() {
    return LOGGER;
  }

  public void info(String message, Object... args) {
    System.out.printf(message, args);
    System.out.println();
  }

  public void error(String message, Object... args) {
    System.err.printf(message, args);
    System.out.println();
  }
}
