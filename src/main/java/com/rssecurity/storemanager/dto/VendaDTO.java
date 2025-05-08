package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record VendaDTO(
        Long idVenda,
        LocalDateTime data,
        String observacao,
        UsuarioResumoDTO usuario,
        @NotNull
        List<ItemVendaDTO> itens
) {
    public VendaDTO {
        if (observacao == null || observacao.trim().equals("")) {
            observacao = "VENDA NO BALCÃO";
        }

        if (data == null) {
            data = LocalDateTime.now();
        }
    }
}
