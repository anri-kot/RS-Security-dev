package com.rssecurity.storemanager.excel.headers;

public enum ProdutoExcelHeader {
    ID("id"),
    NOME("nome"),
    PRECO_ATUAL("preco atual"),
    DESCRICAO("descricao"),
    ESTOQUE("estoque"),
    ID_CATEGORIA("id categoria"),
    NOME_CATEGORIA("nome categoria");

    private final String headerName;

    ProdutoExcelHeader(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }
}