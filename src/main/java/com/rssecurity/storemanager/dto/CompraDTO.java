package com.rssecurity.storemanager.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CompraDTO(
        Long idCompra,
        @NotNull(message = "data não pode ser nula")
        LocalDateTime data,
        @Size(max = 255, message = "observação não pode ter mais de 255 caracteres")
        String observacao,
        FornecedorDTO fornecedor,
        List<ItemCompraDTO> itens) {

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemCompraDTO item : itens) {
            BigDecimal unitPrice = item.valorUnitario().setScale(2, RoundingMode.HALF_UP);
            int quantity = item.quantidade();

            BigDecimal finalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

            finalPrice = finalPrice.setScale(2, RoundingMode.HALF_UP);
            total = total.add(finalPrice);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
