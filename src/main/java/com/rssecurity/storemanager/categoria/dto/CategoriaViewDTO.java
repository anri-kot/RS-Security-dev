package com.rssecurity.storemanager.categoria.dto;

import java.util.List;

import com.rssecurity.storemanager.infra.view.dto.PageableView;

public record CategoriaViewDTO(
    List<CategoriaDTO> categorias,

    int currentPage,
    int totalPages,
    int size
) implements PageableView {
    
}
