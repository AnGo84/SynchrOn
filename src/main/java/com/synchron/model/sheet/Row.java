package com.synchron.model.sheet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by AnGo on 17.01.2018.
 */

@XmlRootElement
public class Row {
    private int rowId;
    private List<Cell> cells;

    public Row() {
    }

    public Row(int rowId, List<Cell> cells) {
        this.rowId = rowId;
        this.cells = cells;
    }
    @XmlAttribute
    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }
    @XmlElement(name = "cell")
    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
