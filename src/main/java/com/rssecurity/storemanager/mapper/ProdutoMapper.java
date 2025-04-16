package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);
    ProdutoDTO toDTO(Produto produto);
    Produto toEntity(ProdutoDTO dto);
}
