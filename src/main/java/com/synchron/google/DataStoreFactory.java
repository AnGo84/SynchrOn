package com.synchron.google;

import com.google.api.client.util.store.FileDataStoreFactory;
import com.synchron.google.DataStoreDir;

import java.io.IOException;

/**
 * Created by AnGo on 25.05.2017.
 */
/**
 * Global instance of the {@link FileDataStoreFactory}.
 */
public class DataStoreFactory extends FileDataStoreFactory {
    private static DataStoreDir dataStoreDir = null;
    private static DataStoreFactory dataStoreFactory = null;

    public static DataStoreFactory getInstance(DataStoreDir dataStoreDir) throws IOException {
        if (!isEqualsDataStoreDir(dataStoreDir) || dataStoreFactory == null) {
            dataStoreFactory = new DataStoreFactory(dataStoreDir);
        }
        return dataStoreFactory;
    }

    private static boolean isEqualsDataStoreDir(DataStoreDir dataStoreDir) {
        if (DataStoreFactory.dataStoreDir == null || dataStoreDir == null)
            return false;
        return DataStoreFactory.dataStoreDir.equals(dataStoreDir);
    }

    private DataStoreFactory(DataStoreDir dataStoreDir) throws IOException {
        super(dataStoreDir);
        this.dataStoreDir = dataStoreDir;
    }
}
