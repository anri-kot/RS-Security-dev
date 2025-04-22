package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemVendaDTO(
        Long idItem,
        @NotNull
        Integer quantidade,
        @NotNull
        BigDecimal valorUnitario,
        BigDecimal desconto,
        ProdutoDTO produto
) {
}
