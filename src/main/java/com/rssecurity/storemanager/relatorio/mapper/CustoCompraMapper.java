package com.rssecurity.storemanager.relatorio.mapper;

import com.rssecurity.storemanager.relatorio.dto.CustoCompraDTO;
import com.rssecurity.storemanager.relatorio.model.CustoCompra;

public class CustoCompraMapper {
    public static CustoCompraDTO toDTO(CustoCompra entity) {
        return new CustoCompraDTO(
            entity.getIdProduto(),
            entity.getData(),
            entity.getCustoTotal()
        );
    }
}
