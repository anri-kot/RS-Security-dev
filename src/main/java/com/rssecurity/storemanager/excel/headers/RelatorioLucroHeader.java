package com.rssecurity.storemanager.excel.headers;

import com.rssecurity.storemanager.util.FormatterUtil;

public enum RelatorioLucroHeader implements ExcelHeader {
    ID("id", false),
    DATA("data", false),
    RECEITA("receita", true),
    MEDIA_CUSTO("media custo", true),
    MEDIA_LUCRO("media lucro", true),
    PORCEN("%", false);

    private final String headerName;
    private final boolean isNumeric;

    RelatorioLucroHeader(String headerName, boolean isNumeric) {
        this.headerName = headerName;
        this.isNumeric = isNumeric;
    }

    @Override
    public String getHeaderName() {
        return headerName;
    }

    @Override
    public String getKey() {
        return switch (this) {
            case ID -> "idProduto";
            case PORCEN -> "lucroPercentual";
            default -> FormatterUtil.formatCamelCase(headerName);
        };
    }

    @Override
    public boolean isNumeric() {
        return isNumeric;
    }
    
}
