package com.synchron.model;

import javafx.beans.property.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by AnGo on 13.06.2017.
 */
@XmlType(propOrder = {"sheetId", "index", "title", "userName","type","frozenRow","export"})
@XmlRootElement(name = "Sheet")
public class DocSheet implements Serializable {
    private IntegerProperty sheetId = new SimpleIntegerProperty();
    private IntegerProperty index = new SimpleIntegerProperty();
    private StringProperty title = new SimpleStringProperty();
    private StringProperty userName = new SimpleStringProperty();
    private StringProperty type = new SimpleStringProperty();
    private IntegerProperty frozenRow = new SimpleIntegerProperty();
    private BooleanProperty export = new SimpleBooleanProperty();

    @XmlElement(name = "SheetId")
    public int getSheetId() {
        return sheetId.get();
    }

    public IntegerProperty sheetIdProperty() {
        return sheetId;
    }

    public void setSheetId(int sheetId) {
        this.sheetId.set(sheetId);
    }

    @XmlElement(name = "Index")
    public int getIndex() {
        return index.get();
    }

    public IntegerProperty indexProperty() {
        return index;
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    @XmlElement(name = "Title")
    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    @XmlElement(name = "Type")
    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    @XmlElement(name = "Frozen_Row")
    public int getFrozenRow() {
        return frozenRow.get();
    }

    public IntegerProperty frozenRowProperty() {
        return frozenRow;
    }

    public void setFrozenRow(int frozenRow) {
        this.frozenRow.set(frozenRow);
    }
    @XmlElement(name = "User_Name")
    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    @XmlElement(name = "Export")
    public boolean isExport() {
        return export.get();
    }

    public BooleanProperty exportProperty() {
        return export;
    }

    public void setExport(boolean export) {
        this.export.set(export);
    }

    public String getExportSheetName() {
        return ((userName == null || userName.get().equals("")) ? title.get() : userName.get());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DocSheet{");
        sb.append("sheetId=").append(sheetId);
        sb.append(", index=").append(index);
        sb.append(", title=").append(title);
        sb.append(", userName=").append(userName);
        sb.append(", type=").append(type);
        sb.append(", frozenRow=").append(frozenRow);
        sb.append(", export=").append(export);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocSheet docSheet = (DocSheet) o;

        if (!sheetId.equals(docSheet.sheetId)) return false;
        if (!index.equals(docSheet.index)) return false;
        if (!title.equals(docSheet.title)) return false;
        if (userName != null ? !userName.equals(docSheet.userName) : docSheet.userName != null) return false;
        if (type != null ? !type.equals(docSheet.type) : docSheet.type != null) return false;
        if (frozenRow != null ? !frozenRow.equals(docSheet.frozenRow) : docSheet.frozenRow != null) return false;
        return export != null ? export.equals(docSheet.export) : docSheet.export == null;
    }

    @Override
    public int hashCode() {
        int result = sheetId.hashCode();
        result = 31 * result + index.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (frozenRow != null ? frozenRow.hashCode() : 0);
        result = 31 * result + (export != null ? export.hashCode() : 0);
        return result;
    }
}
