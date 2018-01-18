package com.synchron.model.sheet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by AnGo on 17.01.2018.
 */
@XmlRootElement
public class Sheet {
    private List<Row> rows;

    public Sheet() {
    }

    public Sheet(List<Row> rows) {
        this.rows = rows;
    }
    @XmlElementWrapper(name = "rows")
    @XmlElement(name = "row")
    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}
