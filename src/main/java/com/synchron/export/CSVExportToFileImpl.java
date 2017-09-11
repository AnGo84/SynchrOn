package com.synchron.export;

import com.google.api.services.sheets.v4.Sheets;
import com.synchron.google.GoogleSheetIOHandler;
import com.synchron.model.DocSheet;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by AnGo on 09.09.2017.
 */
public class CSVExportToFileImpl implements ExportToFile{
    private static final String FILE_TYPE = "CSV";

    @Override
    public String getExportFileType() {
        return FILE_TYPE;
    }

    @Override
    public String getExportFileName(String fileWithoutType) {
        return fileWithoutType + "." + FILE_TYPE.toLowerCase();
    }

    @Override
    public void exportToFile(File file, ExportDataList exportData) throws IOException {
        CSVFileWriter.writeValuesToCSVFile(file, exportData.getDataList());
    }

    @Override
    public void exportToFile(String fileWithoutType, Sheets service, String spreadSheetId, List<DocSheet> docSheetList) throws IOException {
        if (docSheetList != null) {
            for (DocSheet docSheet : docSheetList) {
                if (docSheet.isExport()) {
                    List<String[]> stringsValues = GoogleSheetIOHandler.getStringsValuesList(service, spreadSheetId, docSheet.getTitle());
                    stringsValues = excludeFrozenRows(stringsValues, docSheet.getFrozenRow());
                    CSVFileWriter.writeValuesToCSVFile(new File(getExportFileName(fileWithoutType + "_" + docSheet.getExportSheetName())), stringsValues);
                }
            }
        }
    }

    private static List<String[]> excludeFrozenRows(List<String[]> strings, int frozenRows) {
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
}
