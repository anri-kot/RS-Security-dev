package com.rssecurity.storemanager.excel.headers;

import com.rssecurity.storemanager.util.FormatterUtil;

public enum CategoriaExcelHeader implements ExcelHeader {
    ID("id"),
    NOME("nome");

    private final String headerName;

    CategoriaExcelHeader(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }

    @Override
    public String getKey() {
        return switch (this) {
            case ID -> "idCategoria";
            default -> FormatterUtil.formatCamelCase(headerName);
        };
    }

    // Has no numeric values
    @Override
    public boolean isNumeric() {
        return false;
    }
}
