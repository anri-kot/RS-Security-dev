package com.rssecurity.storemanager.compra.dto;

import java.util.List;

import com.rssecurity.storemanager.categoria.dto.CategoriaDTO;
import com.rssecurity.storemanager.infra.view.dto.PageableView;

public record CompraViewDTO(
    List<CompraDTO> compras,
    List<CategoriaDTO> categorias,

    int currentPage,
    int totalPages,
    int size
) implements PageableView{
    
}
