package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CompraDTO(
        Long idCompra,
        @NotNull LocalDateTime data,
        String observacao,
        FornecedorDTO fornecedor,
        List<ItemCompraDTO> itens) {

    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal finalPrice;

        for (ItemCompraDTO item : itens) {
            finalPrice = item.valorUnitario().multiply(BigDecimal.valueOf(item.quantidade()));
            total = total.add(finalPrice);
        }
        return total;
    }
}
