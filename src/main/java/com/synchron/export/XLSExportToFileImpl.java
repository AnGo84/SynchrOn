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
public class XLSExportToFileImpl implements ExportToFile {
    private static final String FILE_TYPE = "XLS";

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
        XLSFileWriter.writeValuesToFile(file, exportDataList);
    }

    @Override
    public void exportToFile(String fileWithoutType, Sheets service, String spreadSheetId, List<DocSheet> docSheetList) throws IOException {
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
        File file = new File(getExportFileName(fileWithoutType));
        XLSFileWriter.writeValuesToFile(file, exportDataList);
    }
}
