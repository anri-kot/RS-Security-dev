package com.rssecurity.storemanager.relatorio.mapper;

import com.rssecurity.storemanager.relatorio.dto.LucroVendaDTO;
import com.rssecurity.storemanager.relatorio.model.LucroVenda;

public class LucroVendaMapper {
    public static LucroVendaDTO toDTO(LucroVenda entity) {
        return new LucroVendaDTO(
            entity.getIdVenda(),
            entity.getData(),
            entity.getReceitaTotal(),
            entity.getCustoTotal(),
            entity.getLucroTotal(),
            entity.getLucroPercentual()
        );
    }
}
