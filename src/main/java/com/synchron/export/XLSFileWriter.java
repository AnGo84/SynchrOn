package com.synchron.export;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by AnGo on 06.06.2017.
 */

/**
 * Some tutorial: http://howtodoinjava.com/apache-commons/readingwriting-excel-files-in-java-poi-tutorial/
 */
public class XLSFileWriter {

    private static void writeListToHSSFSheet(HSSFSheet sheet, List<String[]> stringsList) throws IOException {
        //Iterate over data and write to model
        int rowNum = 0;
        // Add header
//        rowNum = fillRowFromStrings(model, rowNum, EmployeeHandler.FIELDS_NAMES);
        for (String[] strings : stringsList) {
            rowNum = fillRowFromStrings(sheet, rowNum, strings);
        }

    }


    public static void writeValuesToFile(File file, List<ExportData> exportDataList ) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            //Blank workbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            for (ExportData exportData : exportDataList) {
                HSSFSheet model = workbook.createSheet(exportData.getName());
                XLSFileWriter.writeListToHSSFSheet(model, exportData.getDataList());
            }
            //Write the workbook in file system
            workbook.write(out);
            //System.out.println("XLS File '" + file.getAbsolutePath() + "' written successfully on disk.");
        }
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
}
