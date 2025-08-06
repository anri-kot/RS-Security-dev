package com.rssecurity.storemanager.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProdutoDTO(
    Long idProduto,
    @NotBlank(message = "não pode estar em branco")
    @Size(min = 2, max = 255, message = "deve ter entre 2 e 255 caracteres")
    String nome,
    @Size(max = 50, message = "deve ter no máximo 50 caracteres")
    String codigoBarras,
    BigDecimal precoAtual,
    @Size(max = 155, message = "deve ter no máximo 155 caracteres")
    String descricao,
    @NotNull(message = "não pode ser nulo")
    Integer estoque,
    CategoriaDTO categoria
    ) {
}
