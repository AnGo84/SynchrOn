package com.synchron.custom;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

/**
 * Created by AnGo on 15.06.2017.
 */
public class LocalDateTimeAdapter extends XmlAdapter {
//    @Override
//    public LocalDateTime unmarshal(String v) throws Exception {
//        return LocalDateTime.parse(v);
//    }
//
//    @Override
//    public String marshal(LocalDateTime v) throws Exception {
//        return v.toString();
//    }

    @Override
    public Object unmarshal(Object v) throws Exception {
        return LocalDateTime.parse(v.toString());
    }

    @Override
    public Object marshal(Object v) throws Exception {
        if (v == null) {
            return null;
        } else {
            return v.toString();
        }

    }
}
