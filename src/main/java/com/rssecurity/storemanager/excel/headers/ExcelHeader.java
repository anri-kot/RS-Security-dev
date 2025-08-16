package com.rssecurity.storemanager.excel.headers;

public interface ExcelHeader {
    String getHeaderName();
    String getKey();
    boolean isNumeric();
}
