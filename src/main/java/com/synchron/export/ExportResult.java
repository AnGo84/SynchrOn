package com.synchron.export;

/**
 * Created by AnGo on 07.07.2017.
 */
public enum ExportResult {

    SUCCESS("success"),
    FAIL("fail"),
    UNDEFINED("undefined");

    private String label;

    ExportResult(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }

}
