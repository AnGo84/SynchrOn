package com.synchron.google;

/**
 * Created by AnGo on 25.05.2017.
 */

/**
 * Directory to store user credentials for this application.
 */
public class DataStoreDir extends java.io.File {
    private static String projectName = null;
    private static DataStoreDir dataStoreDir = null;

    public static DataStoreDir getInstance(String projectName) {
        if (!isEqualProjectName(projectName) || dataStoreDir == null) {
            dataStoreDir = new DataStoreDir(projectName);
        }
        return dataStoreDir;
    }

    private DataStoreDir(String projectName) {
        super(System.getProperty("user.home"), ".credentials/sheets.googleapis." + projectName);
        this.projectName = projectName;
    }

    private static boolean isEqualProjectName(String projectName) {
        if (DataStoreDir.projectName == null || projectName == null)
            return false;
        return DataStoreDir.projectName.equals(projectName);
    }
}
