package com.synchron.export;

/**
 * Created by AnGo on 20.06.2017.
 */
public enum ExportType {
    CSV("CSV"),
    XLS("XLS");

    private String typeName;

    ExportType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
