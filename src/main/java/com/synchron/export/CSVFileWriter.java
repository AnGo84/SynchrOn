package com.synchron.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by AnGo on 19.04.2017.
 */
public class CSVFileWriter {
    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";


    public static void writeValuesToCSVFile(File file, List<String[]> stringList) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (String[] strings : stringList) {
                if (strings != null && strings.length > 0) {
                    fileWriter.append(strings[0]);
                    for (int i = 1; i < strings.length; i++) {
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(strings[i]);
                    }
                }
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
        }
    }

}
