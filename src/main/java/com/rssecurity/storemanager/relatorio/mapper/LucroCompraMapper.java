package com.rssecurity.storemanager.relatorio.mapper;

import com.rssecurity.storemanager.relatorio.dto.LucroCompraDTO;
import com.rssecurity.storemanager.relatorio.model.LucroCompra;

public class LucroCompraMapper {
    public static LucroCompraDTO toDTO(LucroCompra entity) {
        return new LucroCompraDTO(
            entity.getIdProduto(),
            entity.getData(),
            entity.getCustoTotal()
        );
    }
}
