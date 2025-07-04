package com.rssecurity.storemanager.excel.headers;

public enum VendaExcelHeader {
    ID("id"),
    DATA("data"),
    OBSERVACAO("observacao"),
    USERNAME("username"),
    METODO_PAGAMENTO("metodo pagamento"),
    VALOR_RECEBIDO("valor recebido"),
    TROCO("troco"),
    QUANTIDADE("quantidade"),
    VALOR_UNITARIO("valor unitario"),
    DESCONTO("desconto"),
    ID_PRODUTO("id produto"),
    NOME_PRODUTO("nome produto");
    
    private final String headerName;

    VendaExcelHeader(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }
}
