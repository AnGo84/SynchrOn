package com.synchron.google;

import com.synchron.custom.Converter;

/**
 * Created by AnGo on 23.05.2017.
 */
public class SheetProperties {
    private String sheetId;
    private String sheetName;
    //    private String dataRange;
    private String columnFrom;
    private String columnTill;
    private int rowFrom;
    private int columnsCount;

    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getColumnFrom() {
        return columnFrom;
    }

    public void setColumnFrom(String columnFrom) {
        this.columnFrom = columnFrom;
    }

    public String getColumnTill() {
        return columnTill;
    }

    public void setColumnTill(String columnTill) {
        this.columnTill = columnTill;
    }

    public int getRowFrom() {
        return rowFrom;
    }

    public String getStringRowFrom() {
        return String.valueOf(rowFrom);
    }

    public void setRowFrom(int rowFrom) {
        this.rowFrom = rowFrom;
    }

    public void setRowFrom(String rowFrom) {
        this.rowFrom = Converter.getIntFromString(rowFrom);
    }

    public int getColumnsCount() {
        return columnsCount;
    }

    public String getStringColumnsCount() {
        return String.valueOf(columnsCount);
    }

    public void setColumnsCount(int columnsCount) {
        this.columnsCount = columnsCount;
    }

    public void setColumnsCount(String string) {
        this.columnsCount = Converter.getIntFromString(string);
    }

    public String getDataRange() {
        return columnFrom + String.valueOf(rowFrom)+ ":" + columnTill;
    }

    public String getSheetNameWithRange() {
        return sheetName + "!" + getDataRange();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SheetProperties{");
        sb.append("sheetId='").append(sheetId).append('\'');
        sb.append(", sheetName='").append(sheetName).append('\'');
        sb.append(", columnFrom='").append(columnFrom).append('\'');
        sb.append(", columnTill='").append(columnTill).append('\'');
        sb.append(", rowFrom=").append(rowFrom);
        sb.append(", columnsCount=").append(columnsCount);
        sb.append('}');
        return sb.toString();
    }
}
