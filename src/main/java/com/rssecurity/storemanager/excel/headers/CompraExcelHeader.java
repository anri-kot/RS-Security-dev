package com.rssecurity.storemanager.excel.headers;

import com.rssecurity.storemanager.util.FormatterUtil;

public enum CompraExcelHeader implements ExcelHeader {
    ID("id", false),
    DATA("data", false),
    OBSERVACAO("observacao", false),
    ID_FORNECEDOR("id fornecedor", false),
    NOME_FORNECEDOR("nome fornecedor", false),
    ID_PRODUTO("id produto", false),
    NOME_PRODUTO("nome produto", false),
    QUANTIDADE("quantidade", true),
    VALOR_UNITARIO("valor unitario", true);

    private final String headerName;
    private final boolean numeric;

    CompraExcelHeader(String headerName, boolean numeric) {
        this.headerName = headerName;
        this.numeric = numeric;
    }

    @Override
    public String getHeaderName() {
        return headerName;
    }

    @Override
    public boolean isNumeric() {
        return numeric;
    }

    @Override
    public String getKey() {
        return switch (this) {
            case ID -> "idCompra";
            default -> FormatterUtil.formatCamelCase(headerName);
        };
    }
}
