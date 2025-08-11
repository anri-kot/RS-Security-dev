package com.rssecurity.storemanager.excel.headers;

import com.rssecurity.storemanager.util.FormatterUtil;

public enum ProdutoExcelHeader implements ExcelHeader{
    ID("id", false),
    NOME("nome", false),
    CODIGO_BARRAS("codigo barras", false),
    PRECO_ATUAL("preco atual", false),
    DESCRICAO("descricao", false),
    ESTOQUE("estoque", false),
    ID_CATEGORIA("id categoria", false),
    NOME_CATEGORIA("nome categoria", false);

    private final String headerName;
    private final boolean numeric;

    ProdutoExcelHeader(String headerName, boolean numeric) {
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
            case ID -> "idProduto";
            default -> FormatterUtil.formatCamelCase(headerName);
        };
    }
}