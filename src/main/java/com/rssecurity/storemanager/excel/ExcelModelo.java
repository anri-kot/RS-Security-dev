package com.rssecurity.storemanager.excel;

public enum ExcelModelo {
    GERAL("modelo-geral"),
    PRODUTO("modelo-produto"),
    CATEGORIA("modelo-categoria"),
    FORNECEDOR("modelo-fornecedor"),
    VENDA("modelo-venda"),
    COMPRA("modelo-compra");

    private final String fileName;

    ExcelModelo(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName + ".xlsx";
    }
}
