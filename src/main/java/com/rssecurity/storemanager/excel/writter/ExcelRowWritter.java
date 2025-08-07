package com.rssecurity.storemanager.excel.writter;

public class ExcelRowWritter {
    public static Double tryNumeric(Object obj) {
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static String getObjectAsString(Object obj) {
        return obj.toString();
    }
}