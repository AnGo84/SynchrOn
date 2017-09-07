package com.synchron.properties;


import com.synchron.google.GoogleAPIProject;

import java.io.*;
import java.util.Properties;

/**
 * Created by AnGo on 23.05.2017.
 */
public class PropertiesHandler {
    public static final String APP_XML_FILE = "APP_XML_FILE";
    public static final String GOOGLE_PROJECT_NAME = "GOOGLE_PROJECT_NAME";
    public static final String GOOGLE_PROJECT_JSON = "GOOGLE_PROJECT_JSON";
    public static final String USER_NAME = "USER_NAME";
    public static final String DELAY_AFTER_START = "DELAY_AFTER_START";
    public static final int DEFAULT_DELAY_AFTER_START = 60;
    public static final String REPEAT_PERIOD = "REPEAT_PERIOD";
    public static final int DEFAULT_REPEAT_PERIOD = 30;


    public static Properties getPropertiesFromFile(File file) throws IOException {
        if (file == null) {
            throw new FileNotFoundException("Properties' file '" + file.getPath() + "'  not found!");
        }
        Properties properties = new Properties();
        if (!file.exists()) {
            //System.out.println("Create file: " +file.getPath());
            file.createNewFile();
        } else {
            try (InputStream inputStream = new FileInputStream(file)) {
                //System.out.println("Read file: " +file.getPath());
                properties.load(inputStream);
            }
        }
        return properties;
    }


    public static GoogleAPIProject getGoogleAPIProject(Properties properties) {
        checkProperties(properties);
        GoogleAPIProject googleAPIProject = new GoogleAPIProject();
        googleAPIProject.setUserName(properties.getProperty(USER_NAME, ""));
        googleAPIProject.setProjectName(properties.getProperty(GOOGLE_PROJECT_NAME, ""));
        googleAPIProject.setPathToJson(properties.getProperty(GOOGLE_PROJECT_JSON, ""));
        return googleAPIProject;
    }

    private static void checkProperties(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties cannot be null!!!");
        }
    }

    public static String getPropertyString(Properties properties, String propertyName) {
        return properties.getProperty(propertyName, "");
    }

    public static int getPropertyInt(Properties properties, String propertyName, int defaultValue) {
        int value = defaultValue;
        if (PropertiesHandler.getPropertyString(properties, propertyName) != null && PropertiesHandler.getPropertyString(properties, propertyName).length() > 0) {
            value = Integer.parseInt(PropertiesHandler.getPropertyString(properties, propertyName));
        }
        return value;
    }

    public static String toString(Properties properties) {

        final StringBuilder sb = new StringBuilder("Properties{").append("\n");
        for (final String name : properties.stringPropertyNames()) {
            sb.append(name).append(" : ").append(properties.getProperty(name)).append("\n");
        }
        sb.append('}');
        return sb.toString();
    }
}
