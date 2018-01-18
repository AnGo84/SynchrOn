package com.synchron.export;

import com.google.api.services.sheets.v4.Sheets;
import com.synchron.google.GoogleSheetIOHandler;
import com.synchron.model.DocSheet;
import com.synchron.model.Entries;
import com.synchron.model.Entry;
import com.synchron.model.sheet.Sheet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by AnGo on 10.01.2018.
 */
public class XMLExportToFileImpl implements ExportToFile {
    private static final String FILE_TYPE = "XML";

    @Override
    public String getExportFileType() {
        return FILE_TYPE;
    }

    @Override
    public String getExportFileName(String fileWithoutType) {
        return fileWithoutType + "." + FILE_TYPE.toLowerCase();
    }

    /*@Override
    public void exportToFile(File file, ExportDataList exportData) throws IOException {
        List<Entry> entryList = ExportHandler.getEntryList(exportData);

        try {
            JAXBContext context = JAXBContext.newInstance(Entries.class);
            Entries entries = new Entries(entryList);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Маршаллируем и сохраняем XML в файл.
            marshaller.marshal(entries, file);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }*/

    @Override
    public void exportToFile(File file, ExportDataList exportData) throws IOException {
        Sheet sheet = ExportHandler.getSheet(exportData);

        try {
            JAXBContext context = JAXBContext.newInstance(Sheet.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Маршаллируем и сохраняем XML в файл.
            marshaller.marshal(sheet, file);
        } catch (JAXBException e) {
            throw new IOException(e);
        }

    }


    @Override
    public void exportToFile(String fileWithoutType, Sheets service, String spreadSheetId, List<DocSheet> docSheetList) throws IOException {
        if (docSheetList != null) {
            for (DocSheet docSheet : docSheetList) {
                if (docSheet.isExport()) {
                    List<String[]> stringsValues = GoogleSheetIOHandler.getStringsValuesList(service, spreadSheetId, docSheet.getTitle());
                    //stringsValues = excludeFrozenRows(stringsValues, docSheet.getFrozenRow());
                    String fileName = fileWithoutType + "_" + docSheet.getExportSheetName();
                    ExportDataList exportDataList = new ExportDataList(fileName, stringsValues);
                    exportToFile(new File(getExportFileName(fileName)), exportDataList);
                }
            }
        }
    }
}
