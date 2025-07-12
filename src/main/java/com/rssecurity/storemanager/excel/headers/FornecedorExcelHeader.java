package com.rssecurity.storemanager.excel.headers;

public enum FornecedorExcelHeader {
    ID("id"),
    NOME("nome"),
    CNPJ("cnpj"),
    TELEFONE("telefone"),
    EMAIL("email");

    private final String headerName;

    FornecedorExcelHeader(String headerName) {
        this.headerName = headerName;
    }
    
    public String getHeaderName() {
        return headerName;
    }
}
