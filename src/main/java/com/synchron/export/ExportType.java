package com.synchron.export;

/**
 * Created by AnGo on 20.06.2017.
 */
public enum ExportType {
    CSV("CSV", new CSVExportToFileImpl()),
    XLS("XLS", new XLSExportToFileImpl());

    private String typeName;
    private ExportToFile exportToFile;

    ExportType(String typeName, ExportToFile exportToFile) {
        this.typeName = typeName;
        this.exportToFile = exportToFile;
    }

    public String getTypeName() {
        return typeName;
    }

    public ExportToFile getExportToFile() {
        return exportToFile;
    }
}
