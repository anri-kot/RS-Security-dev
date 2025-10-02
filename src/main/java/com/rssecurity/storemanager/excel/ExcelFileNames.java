package com.rssecurity.storemanager.excel;

public enum ExcelFileNames {
    GERAL("modelo-geral"),
    PRODUTO("modelo-produto"),
    CATEGORIA("modelo-categoria"),
    FORNECEDOR("modelo-fornecedor"),
    VENDA("modelo-venda"),
    COMPRA("modelo-compra");

    private final String fileName;

    ExcelFileNames(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName + ".xlsx";
    }
}
