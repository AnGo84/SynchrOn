package com.synchron.model.sheet;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by AnGo on 17.01.2018.
 */


public class Cell {
    private int cellNom;
    private String text;

    public Cell() {
    }

    public Cell(int cellNom, String text) {
        this.cellNom = cellNom;
        this.text = text;
    }
    @XmlElement(name = "id")
    public int getCellNom() {
        return cellNom;
    }

    public void setCellNom(int cellNom) {
        this.cellNom = cellNom;
    }
    @XmlElement
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
