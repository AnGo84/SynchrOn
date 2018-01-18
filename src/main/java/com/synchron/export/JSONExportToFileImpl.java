package com.synchron.export;

import com.google.api.services.sheets.v4.Sheets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synchron.google.GoogleSheetIOHandler;
import com.synchron.model.DocSheet;
import com.synchron.model.sheet.Sheet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by AnGo on 10.01.2018.
 */
public class JSONExportToFileImpl implements ExportToFile {
    private static final String FILE_TYPE = "JSON";

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
        Sheet sheet = ExportHandler.getSheet(exportData);
        Gson objGson = new GsonBuilder().setPrettyPrinting().create();
        String json = objGson.toJson(sheet);
        try(BufferedWriter out = new BufferedWriter(new FileWriter(file));) {
            out.write(json);
        }
        catch (IOException e)
        {
            throw new IOException(e);
        }

    }


    @Override
    public void exportToFile(String fileWithoutType, Sheets service, String spreadSheetId, List<DocSheet> docSheetList) throws IOException {
        if (docSheetList != null) {
            for (DocSheet docSheet : docSheetList) {
                if (docSheet.isExport()) {
                    List<String[]> stringsValues = GoogleSheetIOHandler.getStringsValuesList(service, spreadSheetId, docSheet.getTitle());
                    String fileName = fileWithoutType + "_" + docSheet.getExportSheetName();
                    ExportDataList exportDataList = new ExportDataList(fileName, stringsValues);
                    exportToFile(new File(getExportFileName(fileName)), exportDataList);
                }
            }
        }
    }
}
