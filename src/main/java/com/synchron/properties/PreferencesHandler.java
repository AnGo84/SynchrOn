package com.synchron.properties;

import com.synchron.MainApp;
import com.synchron.custom.DateUtil;
import com.synchron.google.GoogleAPIProject;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * Created by AnGo on 13.06.2017.
 */
public class PreferencesHandler {
    //    public static final String XML_FILE_PATH = "XML_FILE_PATH";
//    public static final String JSON_FILE_PATH = "JSON_FILE_PATH";
//    public static final String PROJECT_NAME = "PROJECT_NAME";
//    public static final String USER_NAME = "USER_NAME";
    public static final String LAST_FILE_PATH = "LAST_FILE_PATH";

    /**
     * Возвращает preference последний открытый файл.
     * Этот preference считывается из реестра, специфичного для конкретной
     * операционной системы. Если preference не был найден, то возвращается null.
     *
     * @return
     */
    public static File getPreferenceFilePath(Class aClass, String prefName) {
        Preferences prefs = Preferences.userNodeForPackage(aClass);

        String filePath = prefs.get(prefName, null);
        File file;
        if (filePath != null && (file = new File(filePath)).exists()) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * Задаёт путь текущему загруженному файлу. Этот путь сохраняется
     * в реестре, специфичном для конкретной операционной системы.
     *
     * @param file - файл или null, чтобы удалить путь
     */
    public static void setPreferenceFilePath(File file, Class aClass, String prefName) {
        Preferences prefs = Preferences.userNodeForPackage(aClass);
        if (file != null && file.exists()) {
            prefs.put(prefName, file.getPath());

        } else {
            prefs.remove(prefName);
        }
    }

    public static void setPreferenceString(String value, Class aClass, String prefName) {
        Preferences prefs = Preferences.userNodeForPackage(aClass);
        if (value != null) {
            prefs.put(prefName, value);

        } else {
            prefs.remove(prefName);
        }
    }

    public static String getPreferenceString(Class aClass, String prefName) {
        Preferences prefs = Preferences.userNodeForPackage(aClass);
        String value = prefs.get(prefName, null);
        if (value != null) {
            return value;
        } else {
            return "";
        }
    }


//    public static GoogleAPIProject getGoogleAPIProject() {
//        GoogleAPIProject googleAPIProject = new GoogleAPIProject();
//        googleAPIProject.setUserName(getPreferenceString(MainApp.class, USER_NAME));
//        googleAPIProject.setProjectName(getPreferenceString(MainApp.class, PROJECT_NAME));
//        googleAPIProject.setPathToJson(getPreferenceFilePath(MainApp.class,JSON_FILE_PATH).getAbsolutePath());
//        return googleAPIProject;
//    }

}
