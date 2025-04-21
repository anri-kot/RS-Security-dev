package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CompraDTO(
        Long idCompra,
        @NotNull
        LocalDateTime data,
        String observacao,
        FornecedorDTO fornecedor,
        List<ItemCompraDTO>  itens
) {
}
