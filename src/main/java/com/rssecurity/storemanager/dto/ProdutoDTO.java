package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record ProdutoDTO(Long idProduto, @NotBlank(message = "não pode estar em branco") String nome, String codigoBarras, BigDecimal precoAtual, String descricao, @NotNull(message = "não pode ser nulo") Integer estoque, CategoriaDTO categoria) {
}
