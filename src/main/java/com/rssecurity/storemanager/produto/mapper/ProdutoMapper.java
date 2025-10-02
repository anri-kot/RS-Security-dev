package com.rssecurity.storemanager.produto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.rssecurity.storemanager.produto.dto.ProdutoDTO;
import com.rssecurity.storemanager.produto.model.Produto;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);
    ProdutoDTO toDTO(Produto produto);
    Produto toEntity(ProdutoDTO dto);
}
