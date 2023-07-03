package org.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHandler {
    private Properties configProps;

    public ConfigHandler(){
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            configProps = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            configProps.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

        public String getProperty(String key){
            return this.configProps.getProperty(key);
        }
    }

