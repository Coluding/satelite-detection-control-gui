package org.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigHandler {
    private Properties configProps;
    private final String propFileName = "config.properties";

    public ConfigHandler() {
        configProps = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + propFileName);
                return;
            }
            configProps.load(input);
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
        try (OutputStream output = new FileOutputStream(getClass().getClassLoader().getResource(propFileName).getFile())) {
            configProps.store(output, "Updated properties");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
