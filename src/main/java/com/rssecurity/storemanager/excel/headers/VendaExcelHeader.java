package com.rssecurity.storemanager.excel.headers;

import com.rssecurity.storemanager.util.FormatterUtil;

public enum VendaExcelHeader implements ExcelHeader {
    ID("id", false),
    DATA("data", false),
    OBSERVACAO("observacao", false),
    USERNAME("username", false),
    METODO_PAGAMENTO("metodo pagamento", false),
    VALOR_RECEBIDO("valor recebido", true),
    TROCO("troco", true),
    QUANTIDADE("quantidade", true),
    VALOR_UNITARIO("valor unitario", true),
    DESCONTO("desconto", true),
    ID_PRODUTO("id produto", false),
    NOME_PRODUTO("nome produto", false);
    
    private final String headerName;
    private final boolean numeric;

    VendaExcelHeader(String headerName, boolean numeric) {
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
            case ID -> "idVenda";
            default -> FormatterUtil.formatCamelCase(headerName);
        };
    }
}
