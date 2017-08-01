package com.synchron.export;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnGo on 21.06.2017.
 */
public class ExportData {
    private String name;
    private List<String[]> dataList = new ArrayList<>();

    public ExportData() {
    }

    public ExportData(String name, List<String[]> dataList) {
        this.name = name;
        this.dataList = dataList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String[]> getDataList() {
        return dataList;
    }

    public void setDataList(List<String[]> dataList) {
        this.dataList = dataList;
    }
}
