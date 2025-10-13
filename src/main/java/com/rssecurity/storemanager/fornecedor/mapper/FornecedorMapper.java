package com.rssecurity.storemanager.fornecedor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.rssecurity.storemanager.fornecedor.dto.FornecedorDTO;
import com.rssecurity.storemanager.fornecedor.model.Fornecedor;

@Mapper(componentModel = "spring")
public interface FornecedorMapper {

    FornecedorMapper INSTANCE = Mappers.getMapper(FornecedorMapper.class);
    FornecedorDTO toDTO(Fornecedor fornecedor);
    Fornecedor toEntity(FornecedorDTO dto);
}
