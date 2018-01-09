package com.synchron.export;

import com.google.api.services.sheets.v4.Sheets;
import com.synchron.google.GoogleSheetIOHandler;
import com.synchron.model.DocSheet;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnGo on 06.06.2017.
 */

/**
 * Some tutorial: http://howtodoinjava.com/apache-commons/readingwriting-excel-files-in-java-poi-tutorial/
 */
public class ExcelFileWriter {

    private static void writeListToHSSFSheet(HSSFSheet sheet, List<String[]> stringsList) throws IOException {
        //Iterate over data and write to model
        int rowNum = 0;
        // Add header
//        rowNum = fillRowFromStrings(model, rowNum, EmployeeHandler.FIELDS_NAMES);
        for (String[] strings : stringsList) {
            rowNum = fillRowFromStrings(sheet, rowNum, strings);
        }
    }

    private static void writeListToXSSFSheet(XSSFSheet sheet, List<String[]> stringsList) throws IOException {
        //Iterate over data and write to model
        int rowNum = 0;
        // Add header
//        rowNum = fillRowFromStrings(model, rowNum, EmployeeHandler.FIELDS_NAMES);
        for (String[] strings : stringsList) {
            rowNum = fillRowFromStrings(sheet, rowNum, strings);
        }
    }


    public static void writeValuesToXLSFile(File file, List<ExportDataList> exportDataList) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            //Blank workbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            for (ExportDataList exportData : exportDataList) {
                HSSFSheet model = workbook.createSheet(exportData.getFileName());
                ExcelFileWriter.writeListToHSSFSheet(model, exportData.getDataList());
            }
            //Write the workbook in file system
            workbook.write(out);
            //System.out.println("XLS File '" + file.getAbsolutePath() + "' written successfully on disk.");
        }
    }

    public static void writeValuesToXLSXFile(File file, List<ExportDataList> exportDataList) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            //Blank workbook
            XSSFWorkbook workbook = new XSSFWorkbook();
            for (ExportDataList exportData : exportDataList) {
                XSSFSheet model = workbook.createSheet(exportData.getFileName());
                ExcelFileWriter.writeListToXSSFSheet(model, exportData.getDataList());
            }
            //Write the workbook in file system
            workbook.write(out);
            //System.out.println("XLS File '" + file.getAbsolutePath() + "' written successfully on disk.");
        }
    }


    public static List<ExportDataList> getExportData(Sheets service, String spreadSheetId, List<DocSheet> docSheetList) throws IOException {
        List<ExportDataList> exportDataList = new ArrayList<>();
        if (docSheetList != null) {
            for (DocSheet docSheet : docSheetList) {
                if (docSheet.isExport()) {
                    List<String[]> stringsValues = GoogleSheetIOHandler.getStringsValuesList(service, spreadSheetId, docSheet.getTitle());
                    //Create a blank model
                    exportDataList.add(new ExportDataList(docSheet.getExportSheetName(), stringsValues));
                }
            }
        }
        return exportDataList;
    }

    private static int fillRowFromStrings(HSSFSheet sheet, int rowNum, String[] strings) {
        Row row = sheet.createRow(rowNum++);
        int cellNum = 0;
        for (Object obj : strings) {
            Cell cell = row.createCell(cellNum++);
            if (obj instanceof String)
                cell.setCellValue((String) obj);
            else if (obj instanceof Integer)
                cell.setCellValue((Integer) obj);
        }
        return rowNum;
    }

    private static int fillRowFromStrings(XSSFSheet sheet, int rowNum, String[] strings) {
        Row row = sheet.createRow(rowNum++);
        int cellNum = 0;
        for (Object obj : strings) {
            Cell cell = row.createCell(cellNum++);
            if (obj instanceof String)
                cell.setCellValue((String) obj);
            else if (obj instanceof Integer)
                cell.setCellValue((Integer) obj);
        }
        return rowNum;
    }
}
