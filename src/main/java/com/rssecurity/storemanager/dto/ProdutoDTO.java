package com.rssecurity.storemanager.dto;

public record ProdutoDTO(Long idProduto, String nome, String descricao, Integer estoqueMin, Long idCategoria) {
}
