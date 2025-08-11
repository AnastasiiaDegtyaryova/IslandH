package org.example.config;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new ConfigurationException("Configuration file not found: config.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load configuration file: config.properties", e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new ConfigurationException("Missing configuration key: " + key);
        }
        return value;
    }

    public static int getIntProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new ConfigurationException("Missing configuration key: " + key);
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Invalid format for configuration key: " + key, e);
        }
    }
}

