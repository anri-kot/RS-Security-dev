package com.rssecurity.storemanager.excel.headers;

public enum CompraExcelHeader {
    ID("id"),
    DATA("data"),
    OBSERVACAO("observacao"),
    ID_FORNECEDOR("id fornecedor"),
    NOME_FORNECEDOR("nome fornecedor"),
    ID_PRODUTO("id produto"),
    NOME_PRODUTO("nome produto"),
    QUANTIDADE("quantidade"),
    VALOR_UNITARIO("valor unitario");

    private final String headerName;

    CompraExcelHeader(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }
}
