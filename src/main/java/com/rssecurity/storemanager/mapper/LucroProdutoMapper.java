package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.LucroProdutoDTO;
import com.rssecurity.storemanager.model.LucroProduto;

public class LucroProdutoMapper {
    
    public static LucroProdutoDTO toDTO(LucroProduto entity) {
        return new LucroProdutoDTO(
            entity.getIdProduto(),
            entity.getNome(), 
            entity.getCusto(), 
            entity.getReceita(),
            entity.getLucro());
    }

    public static LucroProduto toEntity(LucroProdutoDTO dto) {
        return new LucroProduto(dto.idProduto(), dto.nome(), dto.receita(), dto.custo(), dto.lucro());
    }
}