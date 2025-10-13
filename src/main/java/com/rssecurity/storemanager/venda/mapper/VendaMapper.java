package com.rssecurity.storemanager.venda.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rssecurity.storemanager.usuario.mapper.UsuarioResumoMapper;
import com.rssecurity.storemanager.venda.dto.VendaDTO;
import com.rssecurity.storemanager.venda.model.Venda;

@Mapper(componentModel = "spring", uses = { UsuarioResumoMapper.class })
public interface VendaMapper {
    VendaDTO toDTO(Venda venda);
    @Mapping(target = "itens", ignore = true)
    Venda toEntity(VendaDTO toDTO);
}