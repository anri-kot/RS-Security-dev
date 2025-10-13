package com.rssecurity.storemanager.categoria.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.rssecurity.storemanager.categoria.dto.CategoriaDTO;
import com.rssecurity.storemanager.categoria.model.Categoria;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);
    CategoriaDTO toDTO(Categoria categoria);
    Categoria toEntity(CategoriaDTO dto);
}
