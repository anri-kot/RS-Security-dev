package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemVendaDTO(
        Long idItem,
        @NotNull(message = "quantidade deve ser definida")
        Integer quantidade,
        @NotNull(message = "valor deve ser definido")
        BigDecimal valorUnitario,
        BigDecimal desconto,
        ProdutoDTO produto
) {
}
