package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);
    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    ProdutoDTO toDTO(Produto produto);
    @Mapping(source = "idCategoria", target = "categoria.idCategoria")
    Produto toEntity(ProdutoDTO dto);
}
