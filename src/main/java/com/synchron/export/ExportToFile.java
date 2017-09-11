package com.synchron.export;

import com.google.api.services.sheets.v4.Sheets;
import com.synchron.model.DocSheet;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by AnGo on 06.07.2017.
 */
public interface ExportToFile {
    String getExportFileType();
    String getExportFileName(String fileWithoutType);

    void exportToFile(File file, ExportDataList exportData) throws IOException;

    void exportToFile(String fileWithoutType, Sheets service, String spreadSheetId, List<DocSheet> docSheetList) throws IOException;
}
