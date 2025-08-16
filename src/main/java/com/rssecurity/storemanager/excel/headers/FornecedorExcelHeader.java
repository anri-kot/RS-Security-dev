package com.rssecurity.storemanager.excel.headers;

import com.rssecurity.storemanager.util.FormatterUtil;

public enum FornecedorExcelHeader implements ExcelHeader {
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

    @Override
    public String getKey() {
        return switch (this) {
            case ID -> "idFornecedor";
            default -> FormatterUtil.formatCamelCase(headerName);
        };
    }

    // Has no numeric fields
    @Override
    public boolean isNumeric() {
        return false;
    }
}
