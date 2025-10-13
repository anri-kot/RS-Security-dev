package com.rssecurity.storemanager.produto.dto;

import java.util.List;

import com.rssecurity.storemanager.categoria.dto.CategoriaDTO;
import com.rssecurity.storemanager.infra.view.dto.PageableView;

public record ProdutoViewDTO(
    List<ProdutoDTO> produtos,
    List<CategoriaDTO> categorias,

    int currentPage,
    int totalPages,
    int size
) implements PageableView{
    
}
