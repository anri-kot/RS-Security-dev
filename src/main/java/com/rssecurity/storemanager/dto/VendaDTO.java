package com.rssecurity.storemanager.dto;

import java.math.BigDecimal;
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
        BigDecimal finalPrice;

        for (ItemVendaDTO item : itens) {
            if (item.desconto() != null && item.desconto().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal discountPrice = item.valorUnitario().multiply(item.desconto()
                        .divide(BigDecimal.valueOf(100)));
                finalPrice = item.valorUnitario().subtract(discountPrice)
                        .multiply(BigDecimal.valueOf(item.quantidade()));
            } else {
                finalPrice = item.valorUnitario().multiply(BigDecimal.valueOf(item.quantidade()));
            }
            total = total.add(finalPrice);
        }
        return total;
    }
}
