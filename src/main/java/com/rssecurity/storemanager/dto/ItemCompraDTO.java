package com.rssecurity.storemanager.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemCompraDTO(
        Long idItem,
        @NotNull(message = "quantidade deve ser definida")
        @Size(min = 1, message = "deve haver pelo menos um produto na compra")
        Integer quantidade,
        @NotNull(message = "valor deve ser definido")
        BigDecimal valorUnitario,
        ProdutoDTO produto
) {
}
