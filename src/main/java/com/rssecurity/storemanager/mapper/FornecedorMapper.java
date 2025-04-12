package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.model.Fornecedor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FornecedorMapper {

    FornecedorMapper INSTANCE = Mappers.getMapper(FornecedorMapper.class);
    FornecedorDTO toDTO(Fornecedor fornecedor);
    Fornecedor toEntity(FornecedorDTO dto);
}
