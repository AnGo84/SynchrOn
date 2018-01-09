package com.synchron.export;

import com.google.api.services.sheets.v4.Sheets;
import com.synchron.google.GoogleSheetIOHandler;
import com.synchron.model.DocSheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnGo on 09.09.2017.
 */
public class XLSXExportToFileImpl implements ExportToFile {
    private static final String FILE_TYPE = "XLSX";

    @Override
    public String getExportFileType() {
        return FILE_TYPE;
    }

    @Override
    public String getExportFileName(String fileWithoutType) {
        return fileWithoutType + "." + getExportFileType().toLowerCase();
    }

    @Override
    public void exportToFile(File file, ExportDataList exportData) throws IOException {
        List<ExportDataList> exportDataList = new ArrayList<>();
        exportDataList.add(exportData);
        ExcelFileWriter.writeValuesToXLSXFile(file, exportDataList);
    }

    @Override
    public void exportToFile(String fileWithoutType, Sheets service, String spreadSheetId, List<DocSheet> docSheetList) throws IOException {
        List<ExportDataList> exportDataList = ExcelFileWriter.getExportData(service, spreadSheetId, docSheetList);
        File file = new File(getExportFileName(fileWithoutType));
        ExcelFileWriter.writeValuesToXLSXFile(file, exportDataList);
    }

}
