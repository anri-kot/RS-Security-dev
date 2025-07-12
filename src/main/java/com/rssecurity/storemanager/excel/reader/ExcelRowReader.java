package com.rssecurity.storemanager.excel.reader;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

public class ExcelRowReader {
    private final Map<String, Integer> headerIndexMap;
    private final DataFormatter formatter = new DataFormatter();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public ExcelRowReader(Map<String, Integer> headerIndexMap) {
        this.headerIndexMap = headerIndexMap;
    }

    public String getCellStringValue(Row row, String headerName) {
        Integer index = headerIndexMap.get(headerName.toLowerCase());

        // getLastCellNum() == last cell index + 1
        if (index == null || index >= row.getLastCellNum()) {
            return "";
        }

        Cell cell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        String value = formatter.formatCellValue(cell).trim();

        if (value.isBlank()) return null;
        return value;
    }

    public Long tryGetLong(Row row, String headerName) {
        String value = getCellStringValue(row, headerName);

        if (value == null) return null;

        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(
                    String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
                            headerName, row.getRowNum() + 1, value));
        }
    }

    public BigDecimal tryGetBigDecimal(Row row, String headerName) {
        String value = getCellStringValue(row, headerName);

        if (value == null) return null;

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(
                    String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
                            headerName, row.getRowNum() + 1, value));
        }
    }

    public Integer tryGetInteger(Row row, String headerName) {
        String value = getCellStringValue(row, headerName);

        if (value == null) return null;

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(
                    String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
                            headerName, row.getRowNum() + 1, value));
        }
    }

    public LocalDateTime tryGetLocalDateTime(Row row, String headerName) {
        String value = getCellStringValue(row, headerName);

        if (value == null) return null;

        try {
            return LocalDateTime.parse(value, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
                            headerName, row.getRowNum() + 1, value), value, e.getErrorIndex());
        }
    }
}
