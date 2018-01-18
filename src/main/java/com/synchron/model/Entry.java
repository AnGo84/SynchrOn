package com.synchron.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by AnGo on 10.01.2018.
 */
//@XmlRootElement(name = "Cell")
@XmlType(propOrder = {"id", "text"})
public class Entry implements Serializable {

    private String text;

    private int id;

    public Entry() {
        super();
    }

    @XmlElement
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
