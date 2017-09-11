package com.synchron.export;

import com.google.api.services.sheets.v4.Sheets;
import com.synchron.custom.FileUtils;
import com.synchron.model.GoogleDoc;

import java.io.IOException;
import java.util.Date;

/**
 * Created by AnGo on 20.06.2017.
 */
public class ExportHandler {
    public static final String DEFAULT_EXPORT_FILE_NAME = "SynchrOn";

//    public static void saveExportDataToFile(ExportToFile exportToFile, File file, ExportDataList exportData) throws IOException {
//        exportToFile.exportToFile(file, exportData);
//    }

    public static String getExportFileName(GoogleDoc googleDoc) {
//        String fileName = DEFAULT_EXPORT_FILE_NAME + "." + fileType.getTypeName().toLowerCase();
        if (googleDoc.getName() != null && !googleDoc.getName().equals("")) {
//            fileName = googleDoc.getFileName() + "." + fileType.getTypeName().toLowerCase();
            return googleDoc.getName();
        }
        return "";
    }


    public static void exportGoogleDocToFile(ExportToFile exportToFile, Sheets service, String fileName, GoogleDoc googleDoc) throws IOException, ArithmeticException {
        if (googleDoc == null || exportToFile == null || service == null || fileName.equals("")) {
            throw new ArithmeticException("One or some arguments are not defined");
        }
        try {
            exportToFile.exportToFile(fileName, service, googleDoc.getDocId(), googleDoc.getDocSheets());
            googleDoc.setExportResults(new Date(), ExportResult.SUCCESS);
        } catch (IOException e) {
            googleDoc.setExportResults(new Date(), ExportResult.FAIL);
            throw new IOException(e);
        }
    }


    public static void exportGoogleDoc(Sheets service, GoogleDoc googleDoc, Date timerNowDate) throws IOException, ArithmeticException, IllegalArgumentException {
        ExportType exportType = getExportTypeByName(googleDoc.getExportType());
        String text = checkExportGoogleDoc(service, googleDoc, timerNowDate, exportType);
        if (text.length()!=0){
            throw new IllegalArgumentException("Wrong arguments: " + text);
        }
//        ExportType exportType = ExportType.valueOf(googleDoc.getExportType());
        ExportHandler.exportGoogleDocToFile(exportType.getExportToFile(), service, FileUtils.fileNameWithDir(googleDoc.getExportDir(), googleDoc.getName()), googleDoc);
    }

    private static String checkExportGoogleDoc(Sheets service, GoogleDoc googleDoc, Date timerNowDate, ExportType exportType) {
        String text = "";
        if (service == null){
            text += " Bad connection info;\n";
        }
        if (googleDoc == null){
            text = " Bad Google Doc;\n";
        }

        if (timerNowDate == null ){
            text = " Bad DATE;\n";
        }
        if (exportType == null) {
            text = " Bad Export type;\n";
        }
        return text;
    }

    private static ExportType getExportTypeByName(String typeName) {

        if (typeName == null || typeName.equals("")) {
            return null;
        }
        for (ExportType exportType : ExportType.values()) {
            if (exportType.getTypeName().toUpperCase().equals(typeName.toUpperCase())) {
                return ExportType.valueOf(typeName);
            }
        }
        return null;
    }
}
