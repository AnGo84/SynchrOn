package com.synchron.model;

import com.synchron.custom.LocalDateTimeAdapter;
import com.synchron.export.ExportResult;
import javafx.beans.property.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnGo on 08.06.2017.
 */
@XmlType(propOrder = {"name", "docId", "exportDir", "status", "exportType", "period", "nextSyncDate", "lastSyncDate", "exportResult", "docSheets"})
public class GoogleDoc implements Serializable {
    private StringProperty name = new SimpleStringProperty();
    private StringProperty docId = new SimpleStringProperty();
    private StringProperty exportDir = new SimpleStringProperty();

    private StringProperty status = new SimpleStringProperty();
    private StringProperty exportType = new SimpleStringProperty();
    private IntegerProperty period = new SimpleIntegerProperty();

    private ObjectProperty<LocalDateTime> nextSyncDate = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDateTime> lastSyncDate = new SimpleObjectProperty<>();

    private StringProperty exportResult = new SimpleStringProperty();

    private List<DocSheet> docSheets = new ArrayList<>();


    public GoogleDoc() {
    }

    public GoogleDoc(String name, String docId, String exportDir) {
        this.name.set(name);
        this.docId.set(docId);
        this.exportDir.set(exportDir);
    }

    public GoogleDoc(GoogleDoc googleDoc) {
        this.name.set(googleDoc.getName());
        this.docId.set(googleDoc.getDocId());
        this.exportDir.set(googleDoc.getExportDir());
        this.status.set(googleDoc.getStatus());
        this.exportType.set(googleDoc.getExportType());
        this.period.set(googleDoc.getPeriod());
        this.nextSyncDate.set(googleDoc.getNextSyncDate());
        this.lastSyncDate.set(googleDoc.getLastSyncDate());
        this.exportResult.set(googleDoc.getExportResult());
        this.docSheets = googleDoc.getDocSheets();
    }


    @XmlElement(name = "Name")
    public String getName() {
        return name.get();

    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    @XmlElement(name = "DocumentID")
    public String getDocId() {
        return docId.get();
    }

    public void setDocId(String docId) {
        this.docId.set(docId);
    }

    public StringProperty docIdProperty() {
        return docId;
    }

    @XmlElement(name = "ExportDir")
    public String getExportDir() {
        return exportDir.get();
    }

    public void setExportDir(String exportDir) {
        this.exportDir.set(exportDir);
    }

    public StringProperty exportDirProperty() {
        return exportDir;
    }

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlElement(name = "LastSyncDate")
    public LocalDateTime getLastSyncDate() {
        return lastSyncDate.get();
    }

    public void setLastSyncDate(LocalDateTime lastSyncDate) {
        this.lastSyncDate.set(lastSyncDate);
    }

//    public StringProperty lastSyncDateProperty() {
//        if (lastSyncDate == null) {
//            return null;
//        } else {
//            return new SimpleStringProperty(DateUtil.format(lastSyncDate.getValue()));
//        }
//    }

    public ObjectProperty<LocalDateTime> lastSyncDateProperty() {
        return lastSyncDate;
    }

    @XmlElement(name = "Status")
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    @XmlElement(name = "ExportType")
    public String getExportType() {
        return exportType.get();
    }

    public void setExportType(String exportType) {
        this.exportType.set(exportType);
    }

    public StringProperty exportTypeProperty() {
        return exportType;
    }

    @XmlElement(name = "Period")
    public int getPeriod() {
        return period.get();
    }

    public void setPeriod(int period) {
        this.period.set(period);
    }

    public IntegerProperty periodProperty() {
        return period;
    }

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlElement(name = "NextSyncDate")
    public LocalDateTime getNextSyncDate() {
        return nextSyncDate.get();
    }

    public void setNextSyncDate(LocalDateTime nextSyncDate) {
        this.nextSyncDate.set(nextSyncDate);
    }

    public ObjectProperty<LocalDateTime> nextSyncDateProperty() {
        return nextSyncDate;
    }

    @XmlElement(name = "ExportResult")
    public String getExportResult() {
        return exportResult.get();
    }

    public void setExportResult(String exportResult) {
        this.exportResult.set(exportResult);
    }

    public StringProperty exportResultProperty() {
        return exportResult;
    }

    @XmlElement(name = "SpreadSheet")
    public List<DocSheet> getDocSheets() {
        return docSheets;
    }

    public void setDocSheets(List<DocSheet> docSheets) {
        this.docSheets = docSheets;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GoogleDoc{");
        sb.append("name=").append(name);
        sb.append(", docId=").append(docId);
        sb.append(", exportDir=").append(exportDir);
        sb.append(", status=").append(status);
        sb.append(", exportType=").append(exportType);
        sb.append(", period=").append(period);
        sb.append(", nextSyncDate=").append(nextSyncDate);
        sb.append(", lastSyncDate=").append(lastSyncDate);
        sb.append(", exportResult=").append(exportResult);
        sb.append(", docSheets=").append(docSheets);
        sb.append('}');
        return sb.toString();
    }

    public String toShortString() {
        final StringBuilder sb = new StringBuilder("GoogleDoc{");
        sb.append("name=").append(name);
        sb.append(", exportDir=").append(exportDir);
        sb.append(", status=").append(status);
        sb.append(", exportType=").append(exportType);
        sb.append(", period=").append(period);
        sb.append(", nextSyncDate=").append(nextSyncDate);
        sb.append(", lastSyncDate=").append(lastSyncDate);
        sb.append(", exportResult=").append(exportResult);
        sb.append(", docId=").append(docId);
        sb.append('}');
        return sb.toString();
    }

    public String toInfoString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("name=").append(name.getValueSafe()).append('\n');
        sb.append("docId=").append(docId.getValueSafe()).append('\n');
        sb.append("exportDir=").append(exportDir.getValueSafe()).append('\n');
        sb.append("status=").append(status.getValue()).append('\n');
        sb.append("exportType=").append(exportType.getValue()).append('\n');
        sb.append("period=").append(period.getValue()).append('\n');
        sb.append("nextSyncDate=").append(nextSyncDate.getValue()).append('\n');
        sb.append("lastSyncDate=").append(lastSyncDate.getValue()).append('\n');
        sb.append("exportResult=").append(exportResult.getValue()).append('\n');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleDoc googleDoc = (GoogleDoc) o;

        if (name != null ? !name.equals(googleDoc.name) : googleDoc.name != null) return false;
        if (!docId.equals(googleDoc.docId)) return false;
        if (exportDir != null ? !exportDir.equals(googleDoc.exportDir) : googleDoc.exportDir != null) return false;
        if (status != null ? !status.equals(googleDoc.status) : googleDoc.status != null) return false;
        if (exportType != null ? !exportType.equals(googleDoc.exportType) : googleDoc.exportType != null) return false;
        if (period != null ? !period.equals(googleDoc.period) : googleDoc.period != null) return false;
        if (nextSyncDate != null ? !nextSyncDate.equals(googleDoc.nextSyncDate) : googleDoc.nextSyncDate != null)
            return false;
        if (lastSyncDate != null ? !lastSyncDate.equals(googleDoc.lastSyncDate) : googleDoc.lastSyncDate != null)
            return false;
        if (exportResult != null ? !exportResult.equals(googleDoc.exportResult) : googleDoc.exportResult != null)
            return false;
        return docSheets != null ? docSheets.equals(googleDoc.docSheets) : googleDoc.docSheets == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + docId.hashCode();
        result = 31 * result + (exportDir != null ? exportDir.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (exportType != null ? exportType.hashCode() : 0);
        result = 31 * result + (period != null ? period.hashCode() : 0);
        ;
        result = 31 * result + (nextSyncDate != null ? nextSyncDate.hashCode() : 0);
        result = 31 * result + (lastSyncDate != null ? lastSyncDate.hashCode() : 0);
        result = 31 * result + (exportResult != null ? exportResult.hashCode() : 0);
        result = 31 * result + (docSheets != null ? docSheets.hashCode() : 0);
        return result;
    }

}
