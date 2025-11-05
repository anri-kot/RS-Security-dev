package com.rssecurity.storemanager.excel.importer;

import org.apache.poi.ss.usermodel.Sheet;

public interface ExcelSheetImporter {
    String getSheetName();
    int importSheet(Sheet sheet);
    
}