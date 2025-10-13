package com.rssecurity.storemanager.fornecedor.dto;

import java.util.List;

import com.rssecurity.storemanager.infra.view.dto.PageableView;

public record FornecedorViewDTO(
    List<FornecedorDTO> fornecedores,

    int currentPage,
    int totalPages,
    int size
) implements PageableView {
    
}
