package com.rssecurity.storemanager.excel.headers;

public enum CategoriaExcelHeader {
    ID("id"),
    NOME("nome");

    private final String headerName;

    CategoriaExcelHeader(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }
}
