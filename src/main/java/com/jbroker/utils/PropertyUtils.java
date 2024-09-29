package com.jbroker.utils;

import com.jbroker.exception.JBrokerException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertyUtils {

  private static final String PROPERTIES_FILE = "application.properties";
  private static Properties properties;

  static {
    loadProperties();
  }

  /**
   * Loads the properties from the application.properties file.
   */
  private static void loadProperties() {
    properties = new Properties();
    try (InputStream input = PropertyUtils.class.getClassLoader()
        .getResourceAsStream(PROPERTIES_FILE)) {
      properties.load(input);
    } catch (IOException e) {
      log.error("Failed to load properties from file '{}'. Error: {}",
          PROPERTIES_FILE, e.getMessage());
      throw new JBrokerException(e);
    }
  }

  /**
   * Retrieves a property as an integer, with a default value if not found or invalid.
   *
   * @param key          the property key
   * @param defaultValue the default value to return if the property is missing or invalid
   * @return the property value as an integer, or the default value if not found
   */
  public static int getInt(String key, int defaultValue) {
    try {
      return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
    } catch (NumberFormatException e) {
      log.error("Invalid format for property '{}'. Using default value: {}", key, defaultValue);
      return defaultValue;
    }
  }

  /**
   * Retrieves a property as a String, with a default value if not found.
   *
   * @param key          the property key
   * @param defaultValue the default value to return if the property is missing
   * @return the property value as a String, or the default value if not found
   */
  public static String getString(String key, String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }
}
