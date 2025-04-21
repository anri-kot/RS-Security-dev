package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.model.Compra;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CompraMapper {
    CompraMapper INSTANCE = Mappers.getMapper(CompraMapper.class);
    CompraDTO toDTO(Compra compra);
    Compra toEntity(CompraDTO dto);
}
