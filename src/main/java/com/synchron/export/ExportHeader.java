package com.synchron.export;

import com.google.api.services.sheets.v4.Sheets;
import com.synchron.google.GoogleSheetIOHandler;
import com.synchron.model.DocSheet;
import com.synchron.model.GoogleDoc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnGo on 20.06.2017.
 */
public class ExportHeader {

    public static void saveSheetToXLSFile(String fileWithoutType, Sheets service, String spreadSheetId, List<DocSheet> docSheetList) throws IOException {
        List<ExportData> exportDataList = new ArrayList<>();
        if (docSheetList != null) {
            for (DocSheet docSheet : docSheetList) {
                if (docSheet.isExport()) {
                    List<String[]> stringsValues = GoogleSheetIOHandler.getStringsValuesList(service, spreadSheetId, docSheet.getTitle());
                    //Create a blank model
                    exportDataList.add(new ExportData(docSheet.getExportSheetName(), stringsValues));
                }
            }
        }
        String fileName = fileWithoutType + "." + "xls";
        File file = new File(fileName);
        XLSFileWriter.writeValuesToFile(file, exportDataList);
    }


    public static void saveSheetToCSVFile(String fileWithoutType, Sheets service, String spreadSheetId, List<DocSheet> docSheetList) throws IOException {
        if (docSheetList != null) {
            for (DocSheet docSheet : docSheetList) {
                if (docSheet.isExport()) {
                    List<String[]> stringsValues = GoogleSheetIOHandler.getStringsValuesList(service, spreadSheetId, docSheet.getTitle());

                    stringsValues = excludeFrozenRows(stringsValues, docSheet.getFrozenRow());

                    String fileName = fileWithoutType + "_" + docSheet.getExportSheetName() + "." + "csv";
                    CSVFileWriter.writeValuesToCSVFile(new File(fileName), stringsValues);
                }
            }
        }
    }


    public static List<String[]> excludeFrozenRows(List<String[]> strings, int frozenRows) {
        if (strings == null) {
            return null;
        }
        if (frozenRows != 0) {
            if (frozenRows >= strings.size()) {
                strings.clear();
            }
            while (frozenRows > 0) {
                strings.remove(0);
                frozenRows--;
            }
        }
        return strings;
    }

    public static void exportToFile(Sheets service, ExportType exportType, String file, GoogleDoc googleDoc) throws IOException {
        if (exportType == ExportType.XLS) {
            ExportHeader.saveSheetToXLSFile(file, service, googleDoc.getDocId(), googleDoc.getDocSheets());
        } else if (exportType == ExportType.CSV) {
            ExportHeader.saveSheetToCSVFile(file, service, googleDoc.getDocId(), googleDoc.getDocSheets());
        }
    }

    public static void saveExportDataToFile(ExportType exportType, File file, ExportData exportData) throws IOException {
        if (exportType == ExportType.XLS) {
            List<ExportData> exportDataList = new ArrayList<>();
            exportDataList.add(exportData);
            XLSFileWriter.writeValuesToFile(file, exportDataList);
        } else if (exportType == ExportType.CSV) {
            CSVFileWriter.writeValuesToCSVFile(file, exportData.getDataList());
        }
    }
}
