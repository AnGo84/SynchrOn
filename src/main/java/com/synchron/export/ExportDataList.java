package com.synchron.export;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnGo on 21.06.2017.
 */
public class ExportDataList {
    private String fileName;
    private List<String[]> dataList = new ArrayList<>();

    public ExportDataList() {
    }

    public ExportDataList(String fileName, List<String[]> dataList) {
        this.fileName = fileName;
        this.dataList = dataList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String[]> getDataList() {
        return dataList;
    }

    public void setDataList(List<String[]> dataList) {
        this.dataList = dataList;
    }
}
