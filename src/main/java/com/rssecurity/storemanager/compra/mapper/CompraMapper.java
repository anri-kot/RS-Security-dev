package com.rssecurity.storemanager.compra.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rssecurity.storemanager.compra.dto.CompraDTO;
import com.rssecurity.storemanager.compra.model.Compra;

@Mapper(componentModel = "spring")
public interface CompraMapper {
    CompraDTO toDTO(Compra compra);
    @Mapping(target = "itens", ignore = true)
    Compra toEntity(CompraDTO dto);
}
