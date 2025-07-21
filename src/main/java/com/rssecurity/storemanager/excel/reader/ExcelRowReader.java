package com.rssecurity.storemanager.excel.reader;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Currency;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import com.rssecurity.storemanager.exception.BadRequestException;

public class ExcelRowReader {
    private final String DATE_FORMAT = "dd-MM-yyyy";
    private final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm";
    private final Map<String, Integer> headerIndexMap;
    private final DataFormatter formatter = new DataFormatter();


    public ExcelRowReader(Map<String, Integer> headerIndexMap) {
        this.headerIndexMap = headerIndexMap;
    }

    public boolean isBlankRow(Row row) {
        for (Cell cell : row) {
            if (!formatter.formatCellValue(cell).isBlank()) return false;
        }
        return true;
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

    public String formatIfCurrency(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        try {
            Double.parseDouble(value);
            return value;
        } catch (NumberFormatException ignored) {}

        String cleaned = value.replaceAll("[^\\d,.]", "")
                            .replace(",", ".");

        try {
            double numericValue = Double.parseDouble(cleaned);
            return String.valueOf(numericValue);
        } catch (NumberFormatException e) {
            return "";
        }
    }

    public BigDecimal tryGetBigDecimal(Row row, String headerName) {
        String cellValue = getCellStringValue(row, headerName);

        if (cellValue == null) return null;
        String value = formatIfCurrency(cellValue);

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
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(value, DateTimeFormatter.ofPattern(DATE_FORMAT)).atStartOfDay();
            } catch (BadRequestException ignore) {
                throw new DateTimeParseException(String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
                                headerName, row.getRowNum() + 1, value), value, e.getErrorIndex());
            }
        }
    }
}
