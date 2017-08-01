package com.synchron.fx;

import com.google.api.services.sheets.v4.Sheets;
import com.synchron.custom.DateUtil;
import com.synchron.custom.FileUtils;
import com.synchron.export.ExportHeader;
import com.synchron.export.ExportResult;
import com.synchron.export.ExportType;
import com.synchron.model.GoogleDoc;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by AnGo on 21.06.2017.
 */
public class GoogleDocExport {
    public static final String DEFAULT_EXPORT_FILE_NAME = "SynchrOn";

    public static String getExportFileName(GoogleDoc googleDoc) {
//        String fileName = DEFAULT_EXPORT_FILE_NAME + "." + fileType.getTypeName().toLowerCase();
        if (googleDoc.getName() != null && !googleDoc.getName().equals("")) {
//            fileName = googleDoc.getName() + "." + fileType.getTypeName().toLowerCase();
            return googleDoc.getName();
        }
        return "";
    }

//    public static String getExportDirectory(GoogleDoc googleDoc) {
//        if (googleDoc == null) {
//            return "";
//        }
//        if (googleDoc.getExportDir() != null && (new File(googleDoc.getExportDir()).exists())) {
//            return googleDoc.getExportDir();
//        }
//        return "";
//    }

    public static String fileNameWithoutType(String directoryName, String fileName){
        return directoryName + File.separator + fileName;
    }

    public static String getExportDirectory(GoogleDoc googleDoc) {
        if (googleDoc == null || googleDoc.getExportDir() == null) {
            return "";
        }
        File directory = FileUtils.getDirectory(new File(googleDoc.getExportDir()));
        if (directory != null && !"".equals(directory)) {
            return directory.getAbsolutePath();
        }
        return "";
    }

    public static void exportGoogleDocToFile(Sheets service, ExportType exportType, String fileName, GoogleDoc googleDoc) throws IOException, ArithmeticException {
        if (googleDoc == null || exportType == null || service == null || fileName.equals("")) {
            throw new ArithmeticException("One or some arguments are not defined!");
        }
        try {
            ExportHeader.exportToFile(service, exportType, fileName, googleDoc);
            setExportResults(googleDoc, ExportResult.SUCCESS);
        } catch (IOException e) {
//            googleDoc.setLastSyncDate(DateUtil.getLocalDateTime(new Date()));
//            googleDoc.setExportResult(ExportResult.FAIL.name());
            setExportResults(googleDoc, ExportResult.FAIL);
            throw new IOException(e);
        }
    }

    public static void setExportResults(GoogleDoc googleDoc, ExportResult exportResult) {
        Date date = new Date();
        googleDoc.setLastSyncDate(DateUtil.getLocalDateTime(date));
        googleDoc.setExportResult(exportResult.name());
        Date nextDate = getNextSyncDate(googleDoc, date);
        if (nextDate != null) {
            googleDoc.setNextSyncDate(DateUtil.getLocalDateTime(nextDate));
        }
    }

    private static Date getNextSyncDate(GoogleDoc googleDoc, Date date) {
        Date nextDate = null;
        if (googleDoc.getStatus().toUpperCase().equals("ON")) {
            if (googleDoc.getNextSyncDate() != null) {
                if (googleDoc.getPeriod() > 0) {
                    long minutesPass = (ChronoUnit.MINUTES.between(googleDoc.getNextSyncDate(), DateUtil.getLocalDateTime(date)) / googleDoc.getPeriod()) * googleDoc.getPeriod() + googleDoc.getPeriod();
                    nextDate = DateUtil.addToDate(Date.from(googleDoc.getNextSyncDate().atZone(ZoneId.systemDefault()).toInstant()), (int) (minutesPass * 60));
                }
            } else {
                nextDate = DateUtil.addToDate(date, googleDoc.getPeriod() * 60);
            }
        }
        return nextDate;
    }


}
