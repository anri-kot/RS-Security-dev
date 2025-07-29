package com.rssecurity.storemanager.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public record VendaDTO(
        Long idVenda,
        LocalDateTime data,
        String observacao,
        UsuarioResumoDTO usuario,
        @NotNull List<ItemVendaDTO> itens,
        String metodoPagamento,
        BigDecimal valorRecebido,
        BigDecimal troco) {

    public VendaDTO {
        if (observacao == null || observacao.trim().equals("")) {
            observacao = "VENDA NO BALCÃO";
        }

        if (data == null) {
            data = LocalDateTime.now();
        }
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemVendaDTO item : itens) {
            BigDecimal unitPrice = item.valorUnitario().setScale(2, RoundingMode.HALF_UP);
            int quantity = item.quantidade();

            BigDecimal finalPrice;

            if (item.desconto() != null && item.desconto().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal discountPercent = item.desconto().setScale(2, RoundingMode.HALF_UP);
                BigDecimal discountMultiplier = BigDecimal.ONE.subtract(
                    discountPercent.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                );

                finalPrice = unitPrice.multiply(discountMultiplier)
                                    .multiply(BigDecimal.valueOf(quantity));
            } else {
                finalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
            }

            finalPrice = finalPrice.setScale(2, RoundingMode.HALF_UP);
            total = total.add(finalPrice);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getLiquido() {
        return valorRecebido.subtract(troco).setScale(2, RoundingMode.HALF_UP);
    }
}
