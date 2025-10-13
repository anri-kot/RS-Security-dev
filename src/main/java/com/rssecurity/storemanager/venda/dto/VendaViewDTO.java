package com.rssecurity.storemanager.venda.dto;

import java.util.List;

import com.rssecurity.storemanager.categoria.dto.CategoriaDTO;
import com.rssecurity.storemanager.infra.view.dto.PageableView;

public record VendaViewDTO(
    List<VendaDTO> vendas,
    List<CategoriaDTO> categorias,

    int currentPage,
    int totalPages,
    int size
) implements PageableView {
    
}
