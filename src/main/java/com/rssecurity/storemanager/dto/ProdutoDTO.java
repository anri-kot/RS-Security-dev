package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProdutoDTO(Long idProduto, @NotBlank String nome, BigDecimal precoAtual, String descricao, Integer estoqueMin, CategoriaDTO categoria) {
}
