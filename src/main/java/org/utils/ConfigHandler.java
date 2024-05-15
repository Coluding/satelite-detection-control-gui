package org.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigHandler {
    private Properties configProps;
    private final String propFileName = "config.properties";

    public ConfigHandler() {
        configProps = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            if (input == null) {
                System.out.println("Config file not found, creating a new one with default properties.");
                createDefaultConfig();
            } else {
                configProps.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return this.configProps.getProperty(key);
    }

    public List<String> getListProp(String key) {
        return Arrays.asList(configProps.getProperty(key).split(","));
    }

    public void setProperty(String key, String value) {
        configProps.setProperty(key, value);
    }

    public void saveProperties() {
        try {
            URL resource = getClass().getClassLoader().getResource(propFileName);
            if (resource == null) {
                File file = new File(getClass().getClassLoader().getResource("").getPath() + propFileName);
                try (OutputStream output = new FileOutputStream(file)) {
                    configProps.store(output, "Updated properties");
                }
            } else {
                try (OutputStream output = new FileOutputStream(new File(resource.toURI()))) {
                    configProps.store(output, "Updated properties");
                }
            }
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private void createDefaultConfig() {
        configProps.setProperty("defaultKey1", "defaultValue1");
        configProps.setProperty("defaultKey2", "defaultValue2");
        saveProperties();
    }

    public static void main(String[] args) {
        ConfigHandler configHandler = new ConfigHandler();
        System.out.println("Property: " + configHandler.getProperty("defaultKey1"));
        configHandler.setProperty("newKey", "newValue");
        configHandler.saveProperties();
    }
}
