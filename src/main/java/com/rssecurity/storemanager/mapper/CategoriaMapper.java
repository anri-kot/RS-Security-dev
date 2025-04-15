package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.model.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);
    CategoriaDTO toDTO(Categoria categoria);
    Categoria toEntity(CategoriaDTO dto);
}
