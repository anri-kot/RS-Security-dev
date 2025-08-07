package com.rssecurity.storemanager.excel.headers;

import java.util.ArrayList;
import java.util.List;

public enum VendaExcelHeader implements ExcelHeader {
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

    public List<VendaExcelHeader> numericFields() {
        List<VendaExcelHeader> numericFields = new ArrayList<>();
        numericFields.add(VALOR_RECEBIDO);
        numericFields.add(TROCO);
        numericFields.add(QUANTIDADE);
        numericFields.add(VALOR_UNITARIO);
        numericFields.add(DESCONTO);

        return numericFields;
    }
}
