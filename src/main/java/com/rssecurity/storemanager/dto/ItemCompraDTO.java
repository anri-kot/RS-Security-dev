package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemCompraDTO(
        Long idItem,
        @NotNull
        Integer quantidade,
        @NotNull
        BigDecimal valorUnitario,
        ProdutoDTO produto
) {
}
