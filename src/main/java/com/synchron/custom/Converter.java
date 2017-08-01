package com.synchron.custom;

/**
 * Created by AnGo on 31.05.2017.
 */
public class Converter {

    public static int getIntFromString(String string) {
        if (string == null || string.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(string);
        }
    }

    public static String getStringFromObject(Object obj) {
        return (obj == null ? "" : obj.toString());
    }

}
