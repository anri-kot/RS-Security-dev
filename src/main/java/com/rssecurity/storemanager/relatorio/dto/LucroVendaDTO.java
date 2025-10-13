package com.rssecurity.storemanager.relatorio.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public record LucroVendaDTO(Long idVenda, LocalDateTime data, BigDecimal receitaTotal, BigDecimal custoTotal, BigDecimal lucroTotal, BigDecimal lucroPercentual) {
    public String getLucroPercentualFormatado() {
        if (lucroPercentual == null) return null;

        BigDecimal stripped = lucroPercentual.stripTrailingZeros();
        return stripped.scale() > 0 ? stripped.toPlainString() : stripped.setScale(0, RoundingMode.DOWN).toPlainString();
    }
}
