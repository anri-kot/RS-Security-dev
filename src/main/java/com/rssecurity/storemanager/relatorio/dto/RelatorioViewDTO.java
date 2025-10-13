package com.rssecurity.storemanager.relatorio.dto;

import com.rssecurity.storemanager.infra.view.dto.PageableView;

public record RelatorioViewDTO <T>
(
    String dataInicio,
    String dataFim,
    String target,
    int currentPage,
    int totalPages,
    int size,
    T relatorio // Can be RelatorioVendaDTO or RelatorioCompraDTO
) implements PageableView {

}
