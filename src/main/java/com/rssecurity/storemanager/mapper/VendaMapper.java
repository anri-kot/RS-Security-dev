package com.rssecurity.storemanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.model.Venda;

@Mapper(componentModel = "spring", uses = { UsuarioResumoMapper.class })
public interface VendaMapper {
    VendaDTO toDTO(Venda venda);
    @Mapping(target = "itens", ignore = true)
    Venda toEntity(VendaDTO toDTO);
}