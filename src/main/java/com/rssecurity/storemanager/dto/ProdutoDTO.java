package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotBlank;

public record ProdutoDTO(Long idProduto, @NotBlank String nome, String descricao, Integer estoqueMin, CategoriaDTO categoria) {
}
