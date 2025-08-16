package com.rssecurity.storemanager.excel.writter;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rssecurity.storemanager.excel.headers.ExcelHeader;

public abstract class ExcelWritter<T> {

    protected abstract String getSheetName();
    protected abstract ExcelHeader[] getHeaders();
    protected abstract List<Map<String, Object>> mapData(List<T> data);

    public Workbook export(List<T> data) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(getSheetName());

        // Criar headers
        Row headerRow = sheet.createRow(0);
        ExcelHeader[] headers = getHeaders();
        for (int col = 0; col < headers.length; col++) {
            headerRow.createCell(col, CellType.STRING)
                .setCellValue(headers[col].getHeaderName());
        }

        // Criar linhas
        List<Map<String, Object>> mappedData = mapData(data);
        for (int rowIndex = 0; rowIndex < mappedData.size(); rowIndex++) {
            Map<String, Object> rowData = mappedData.get(rowIndex);
            Row row = sheet.createRow(rowIndex + 1);

            for (int col = 0; col < headers.length; col++) {
                ExcelHeader header = headers[col];
                Object value = rowData.get(header.getKey());

                Cell cell = row.createCell(col);
                if (value != null) {
                    if (header.isNumeric() && value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }
        return workbook;
    }
    
}
