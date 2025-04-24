package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record VendaDTO(
        Long idVenda,
        @NotNull
        LocalDateTime data,
        String observacao,
        UsuarioResumoDTO usuario,
        List<ItemVendaDTO> itens
) {
    public VendaDTO {
        if (observacao == null) {
            observacao = "VENDA NO BALCÃO";
        }
    }
}
